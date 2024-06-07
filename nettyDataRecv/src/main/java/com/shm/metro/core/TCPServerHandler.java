 package com.shm.metro.core;

 import com.shm.metro.kafka.KafkaClient;
 import com.shm.metro.kafka.pool.KafkaConnectionPool;
 import com.shm.metro.protocol.BroadcastProtocol;
 import com.shm.metro.protocol.DataDecode;
 import com.shm.metro.util.Util;
 import io.netty.buffer.ByteBuf;
 import io.netty.channel.ChannelHandlerContext;
 import io.netty.channel.SimpleChannelInboundHandler;
 import io.netty.util.internal.logging.InternalLogger;
 import org.apache.kafka.clients.producer.KafkaProducer;
 import org.apache.kafka.clients.producer.ProducerRecord;
 import org.apache.kafka.clients.producer.RecordMetadata;
 import org.apache.log4j.Logger;

 import java.util.concurrent.ConcurrentHashMap;
 import java.util.concurrent.Future;
 import java.util.concurrent.TimeUnit;

 import static com.shm.metro.util.Util.bytesToHexString;

 public class TCPServerHandler extends SimpleChannelInboundHandler<DataDecode> {


     public static String topic = Util.getConfig().getProperty("kafka_topic");

     protected static final char[] hexArray = "0123456789abcdef".toCharArray();

     static Logger logger = Logger.getLogger("messageLogger");

     private static InternalLogger log = io.netty.util.internal.logging.InternalLoggerFactory.getInstance(TCPServerHandler.class);

     private final BroadcastProtocol protocol;

     private final ConcurrentHashMap<String, Boolean> trainSaveKafka;

     private int kakfaPatotions = Integer.valueOf(Util.getConfig().getProperty("kafka_partitions"));

     public TCPServerHandler(ConcurrentHashMap<String, Boolean> trainSaveKafka, BroadcastProtocol protocol) {
         this.trainSaveKafka = trainSaveKafka;
         this.protocol = protocol;
     }

     public static String bytesToHexString(byte[] bytes) {
         char[] hexChars = new char[bytes.length * 2];
         for (int j = 0; j < bytes.length; j++) {
             int v = bytes[j] & 0xFF;
             hexChars[(j * 2)] = hexArray[(v >>> 4)];
             hexChars[(j * 2 + 1)] = hexArray[(v & 0xF)];
         }
         return new String(hexChars);
     }

     public void writeKafka(DataDecode dataDecode) {
         KafkaProducer<String, DataDecode> m_Producer = null;
         try {
             if (logger.isInfoEnabled()) {
                 logger.info("begin write data " + dataDecode.getTrainData().getTrain_no() + " to kafka");
             }
             if (dataDecode.getTrainData() != null) {

                 int partition = Math.abs(dataDecode.getTrainData().getTrain_no().hashCode() % kakfaPatotions);
                 ProducerRecord<String, DataDecode> data = new ProducerRecord(topic, partition, dataDecode.getTrainData().getTrain_type_no(), dataDecode);

                 m_Producer = KafkaClient.getProducer();
                 if(m_Producer != null) {
                     m_Producer.send(data);
                 }else
                 {
                     logger.error("获取kafka连接失败！");
                     throw new Exception("获取kakfa连接失败");
                 }

             } else {
                 //TODO:车辆信息不存在
                 logger.error("收到的报文，但是车辆信息不存在！");
             }

             if (logger.isInfoEnabled()) {
                 logger.info("end write data " + dataDecode.getTrainData().getTrain_no() + " to kafka");
             }

         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             if(null != m_Producer) {
                 KafkaClient.returnProducerConnection(m_Producer);
             }
         }
     }


     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
         cause.printStackTrace();
     }


     private boolean recordLog = Boolean.parseBoolean(Util.getConfig().getProperty("recored_log"));

     private void saveToLog(String train_no,long time,byte[] buffer,boolean isheatbeart){
         if(recordLog) {
             if(isheatbeart) {
                 logger.warn("receive data,train_no:" + train_no + ",time:" + time + ",heartbeat response:" + bytesToHexString(buffer));
             }else{
                 logger.warn("receive data,train_no:" + train_no + ",time:" + time + ",status response:" + bytesToHexString(buffer));
             }
         }
     }

     @Override
     protected void channelRead0(ChannelHandlerContext ctx, DataDecode msg) throws Exception {
         //TODO:统计每个连接的信息
         //存kafka
         if(msg.getTrainData() != null){
             if(msg.isCRCResult()) {
                 if(msg.getMsgType() == 0x02){
                     ByteBuf buf = ctx.alloc().buffer(msg.getResponseData().length);
                     buf.writeBytes(msg.getResponseData());
                     if(logger.isInfoEnabled()) {
                         logger.info("心跳数据");
                         //logger.info("收到数据："+bytesToHexString(msg.getBuffer()));
                         logger.info("返回数据：" + bytesToHexString(msg.getResponseData()));
                     }
                     //saveToLog(msg.getTrainData().getTrain_no(),msg.getTimestamp(),msg.getResponseData(),true);
                     ctx.writeAndFlush(buf);
                 }
                 else {
                     //存kafka
                     if(trainSaveKafka.get(msg.getTrainData().getTrain_no())){
                         writeKafka(msg);
                     }
                     ByteBuf buf = ctx.alloc().buffer(msg.getResponseData().length);
                     buf.writeBytes(msg.getResponseData());
                     if(logger.isInfoEnabled()) {
                         logger.info("状态数据");
                         //logger.info("收到数据："+bytesToHexString(msg.getBuffer()));
                         logger.info("返回数据：" + bytesToHexString(msg.getResponseData()));
                     }
                     //saveToLog(msg.getTrainData().getTrain_no(),msg.getTimestamp(),msg.getResponseData(),false);
                     ctx.writeAndFlush(buf);
                 }
                 /*ByteBuf buf = ctx.alloc().buffer(msg.getResponseData().length);
                 buf.writeBytes(msg.getResponseData());
                 ctx.writeAndFlush(buf);*/
                /* ByteBuf buf = ctx.alloc().buffer(1);
                 buf.writeInt(0);
                 ctx.writeAndFlush(buf);*/
             }else{

                 ByteBuf buf = ctx.alloc().buffer(1);
                 buf.writeInt(0);
                 ctx.writeAndFlush(buf);
                 logger.error("收到不合法报文！");

             }
         }else{
             ByteBuf buf = ctx.alloc().buffer(1);
             buf.writeInt(0);
             ctx.writeAndFlush(buf);
         }
     }

 }

