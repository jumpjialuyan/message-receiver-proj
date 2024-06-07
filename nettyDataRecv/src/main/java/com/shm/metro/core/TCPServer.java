 package com.shm.metro.core;

 import com.shm.metro.bean.JCLBean;
 import com.shm.metro.bean.TrainData;
 import com.shm.metro.decoder.KnTCPStreamDecoder;
 import com.shm.metro.decoder.TCPStreamDecoder;
 import com.shm.metro.dynamic.DynamicLoadJar;
 import com.shm.metro.enums.NotifyType;
 import com.shm.metro.function.Function;
 import com.shm.metro.interfacer.IProtocol;
 import com.shm.metro.netty.RPCServerInitializer;
 import com.shm.metro.observer.Observer;
 import com.shm.metro.protocol.BroadcastProtocol;
 import com.shm.metro.registry.ServiceRegistry;
 import com.shm.metro.subject.Subject;
 import com.shm.metro.util.HDFSUtil;
 import com.shm.metro.util.MySqlUtil;
 import com.shm.metro.util.Util;
 import io.netty.bootstrap.ServerBootstrap;
 import io.netty.channel.*;
 import io.netty.channel.nio.NioEventLoopGroup;
 import io.netty.channel.socket.SocketChannel;
 import io.netty.channel.socket.nio.NioServerSocketChannel;
 import io.netty.handler.logging.LogLevel;
 import io.netty.handler.logging.LoggingHandler;
 import io.netty.util.internal.logging.InternalLoggerFactory;
 import io.netty.util.internal.logging.Slf4JLoggerFactory;
 import org.apache.log4j.Logger;
 import org.apache.log4j.PropertyConfigurator;

 import java.net.InetAddress;
 import java.net.UnknownHostException;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.*;
 import java.util.concurrent.*;
 import java.util.concurrent.atomic.AtomicBoolean;

 public class TCPServer implements Subject{

     static String[] listen_port = Util.getConfig().getProperty("listen_port").split(",");
     private static List<Integer> listenPorts = new ArrayList<>();

     private JCLBean jclBean = null;

     private volatile BroadcastProtocol broadcast;
     private ServiceRegistry serviceRegistry;

     private List<Observer> channelObservers = new ArrayList<>();


     static {
         for (int i = 0; i < listen_port.length; i++) {
             listenPorts.add(Integer.valueOf(listen_port[i]));
         }
     }

     public  ConcurrentHashMap<String,TrainData> trainDataCache = new ConcurrentHashMap<>();

     static int rpc_port = Integer.valueOf(Util.getConfig().getProperty("rpc_port"));

     static int kangni_listen_port = Integer.valueOf(Util.getConfig().getProperty("kangni_listen_port"));

     private static Logger logger = Logger.getLogger("messageLogger");

     private String zkRegisteData = InetAddress.getLocalHost().getHostName() + ":" + rpc_port;

     //车号接收到的最晚时间戳
     public static ConcurrentHashMap<String,LinkedList> trainResTime = new ConcurrentHashMap<>();

     //车号对应的数据是否存kafka
     public static ConcurrentHashMap<String,Boolean> trainSaveKafka = new ConcurrentHashMap<>();

     public TCPServer() throws UnknownHostException {
         broadcast = new BroadcastProtocol();
         broadcast.setJarPath(HDFSUtil.getNewestJarHDFS());//"hdfs://nameservice1/user/lxb/dataparse.jar");
         broadcast.setUpdateJar(true);
         broadcast.setUpdateJarKey(System.currentTimeMillis());
         broadcast.setUpdateTrainData(true);
         broadcast.setUpdateTrainDataKey(System.currentTimeMillis());
         this.serviceRegistry = new ServiceRegistry(Util.getConfig(),zkRegisteData);
     }


     public static void main(String[] args) throws UnknownHostException {

         try {
             System.setProperty("io.netty.recycler.maxCapacity", "0");
             final TCPServer tcpServer = new TCPServer();
             PropertyConfigurator.configure(tcpServer.getClass().getResourceAsStream("/log4j.properties"));
             InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());
             Thread rpcThread = new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         tcpServer.runRPC(rpc_port);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             });
             rpcThread.start();

             //监听康尼的数据接收线程
             Thread KNTcpThread = new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         tcpServer.runKNTcp(kangni_listen_port);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             });
             KNTcpThread.start();

             //监听协议报文启动方法
             tcpServer.run();

         } catch (Exception e) {
             e.printStackTrace();
             logger.error(e.getMessage());
             System.exit(1);
         }
     }

     //监听康尼的数据接收
     private void runKNTcp(int kn_port) throws Exception {
         ServerBootstrap b = new ServerBootstrap();
         EventLoopGroup bossGroup = null;
         EventLoopGroup workerGroup = null;
         try {
             bossGroup = new NioEventLoopGroup();
             workerGroup = new NioEventLoopGroup();
             b.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(4096))
                     .option(ChannelOption.SO_RCVBUF, Integer.valueOf(5242880))
                     .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(4096))
                     .childOption(ChannelOption.SO_KEEPALIVE, true)
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch)
                                 throws Exception {
                             ChannelPipeline pipeline = ch.pipeline();
                             pipeline.addLast(new KnTCPStreamDecoder());
                             pipeline.addLast("logging", new LoggingHandler(LogLevel.INFO));
                             pipeline.addLast("KNTCPServerHandler", new KnTCPServerHandler());
                         }
                     });

             if(logger.isInfoEnabled()) {
                 logger.info("start KangNiTcpServer on port:" + kn_port);
             }
             Channel KNServerChannel = b.bind(kn_port).sync().channel();
             KNServerChannel.closeFuture().sync();
             if(logger.isInfoEnabled()) {
                 logger.info("Start KangNiTcpServer success,Wating for connection.");
             }
         }catch (Exception e) {
             logger.error(e.getMessage());
             System.exit(1);
         }finally {
             workerGroup.shutdownGracefully();
             bossGroup.shutdownGracefully();
         }
     }

     public void runRPC(int rpc_port) throws Exception {
         EventLoopGroup bossGroup = new NioEventLoopGroup(1);
         EventLoopGroup workerGroup = new NioEventLoopGroup(1);
         try {
             ServerBootstrap b = new ServerBootstrap();
             b.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .childHandler(new RPCServerInitializer(broadcast,this))
                     .option(ChannelOption.SO_BACKLOG, 128)
                     .childOption(ChannelOption.SO_KEEPALIVE, false);

             ChannelFuture f = b.bind(rpc_port).sync();

             this.serviceRegistry.register();

             if(logger.isInfoEnabled()) {
                 logger.info("rpc server start on host:" + InetAddress.getLocalHost().getHostName() + " port: " + rpc_port + "启动");
             }

             f.channel().closeFuture().sync();
         } finally {
             if(this.serviceRegistry.getZk()!=null) {
                 this.serviceRegistry.getZk().close();
             }
             workerGroup.shutdownGracefully();
             bossGroup.shutdownGracefully();
         }
     }

     public void run() throws Exception {
         ServerBootstrap b = new ServerBootstrap();
         EventLoopGroup bossGroup = null;
         EventLoopGroup workerGroup = null;
         updateTrainData();
         final TCPServer that = this;
         try {
             bossGroup = new NioEventLoopGroup();
             workerGroup = new NioEventLoopGroup();
             b.group(bossGroup, workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(8192))
                     .option(ChannelOption.SO_RCVBUF, Integer.valueOf(10485760))
                     .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(8192))
                     .childOption(ChannelOption.SO_KEEPALIVE, true)
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch)
                                 throws Exception {
                             ChannelPipeline pipeline = ch.pipeline();
                             pipeline.addLast(new TCPStreamDecoder(that,broadcast));
                             pipeline.addLast("logging", new LoggingHandler(LogLevel.INFO));
                             pipeline.addLast("TCPServerHandler", new TCPServerHandler(trainSaveKafka,broadcast));
                         }

                     });
             List<Integer> ports = listenPorts;
             Collection<Channel> channels = new ArrayList(ports.size());

             if(logger.isInfoEnabled()) {
                 logger.info("start TcpServer on port:" + ports.toString());
             }

             for (Iterator i$ = ports.iterator(); i$.hasNext(); ) {
                 int port = ((Integer) i$.next()).intValue();
                 Channel ServerChannel = b.bind(port).sync().channel();
                 channels.add(ServerChannel);
             }
             for (Channel ch : channels) {
                 ch.closeFuture().sync();
             }
             if(logger.isInfoEnabled()) {
                 logger.info("Start success,Wating for connection.");
             }
         }catch (Exception e) {
             logger.error(e.getMessage());
             System.exit(1);
         }finally {
             workerGroup.shutdownGracefully();
             bossGroup.shutdownGracefully();
         }
     }

     public synchronized void reloadJclBean() throws Exception{
         JCLBean jclBeanNew = DynamicLoadJar.reloadJCL(broadcast.getJarPath(), Util.getConfig().getProperty("dynamicload_classname"));
         if(jclBeanNew != null){
             jclBean = jclBeanNew;
         }
         IProtocol iProtocol = jclBean.getIProtocol();
         iProtocol = null;
     }

     public synchronized JCLBean getJclBean() {
         if(jclBean == null){
             try {
                 reloadJclBean();
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
         return jclBean;
     }

     public synchronized ConcurrentHashMap<String,TrainData> getTrainData(){
         return trainDataCache;
     }

     public synchronized void updateTrainData() throws Exception{

         final ConcurrentHashMap<String,TrainData> cache = new ConcurrentHashMap<>();

         final AtomicBoolean hasError = new AtomicBoolean(false);

         final StringBuilder error = new StringBuilder();

         String sql = "select *,d.line_name,c.bind_id as protocol_id from metro_train a left join metro_train_type b on a.train_type_id = b.id  " +
                      "left join p_bind_protocol c on c.parent_id = b.id " +
                      "left join metro_line d on b.line_id=d.id " +
                      "where b.del_flag=0";
         MySqlUtil.exec(sql, new Function<ResultSet>() {
             @Override
             public void call(ResultSet v){
                 TrainData trainData = new TrainData();
                 try {
                     trainData.setId(v.getInt("id"));
                     trainData.setTrain_no(v.getString("train_no"));
                     trainData.setTrain_name(v.getString("train_no"));
                     trainData.setTrain_type_name(v.getString("train_type_no"));
                     trainData.setLine_id(v.getInt("line_id"));
                     trainData.setLine_name(v.getString("line_name"));
                     trainData.setOnline_time(v.getDate("online_time"));
                     trainData.setTrain_type_no(v.getString("train_type_no"));
                     trainData.setWheeltrack_type(v.getString("wheeltrack_type"));
                     trainData.setTrain_body_type(v.getString("train_body_type"));
                     trainData.setProtocol_id(v.getInt("protocol_id"));
                     cache.put(v.getString("train_no"),trainData);
                 } catch (SQLException e) {
                     error.append("errorcode:=>"+e.getErrorCode()+",message:"+e.getMessage());
                     logger.error("updateTrainData error=>" + e.getMessage());
                     hasError.set(true);
                     e.printStackTrace();
                 }
             }
         });

         if(!hasError.get()){
             trainDataCache.clear();
             trainDataCache.putAll(cache);
         }else{
             throw new Exception(error.toString());
         }
         logger.error("update trainData info success!");
     }

     @Override
     public void attach(Observer observer) {
        synchronized (channelObservers){
            channelObservers.add(observer);
            logger.error("objserver size => "+channelObservers.size());
        }
     }

     @Override
     public void detach(Observer observer) {
        synchronized (channelObservers){
            channelObservers.remove(observer);
            logger.error("objserver size => "+channelObservers.size());
        }
     }

     @Override
     public void notify(NotifyType message) {
        synchronized (channelObservers){
            for (Observer observer : channelObservers){
                observer.update(message);
            }
        }
     }
 }
