package com.shm.metro.jettyserver;

import com.shm.metro.jettyserver.monitor.MonitorThread;
import com.shm.metro.netty.RPCServerInitializer;
import com.shm.metro.protocol.BroadcastProtocol;
import com.shm.metro.registry.ServiceRegistry;
import com.shm.metro.util.Util;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.InetAddress;

/**
 * Created by Administrator on 2023/9/29.
 * 1.读取已存在的配置信息，查看netty是否运行，返回配置信息及运行状态
 * 2.修改配置信息，更新数据库，重新启动netty，需要用户确认是否重新启动
 * 3.netty 运行时候存pid到/var/run/nettydatarecv.pid
 */
public class ConfigServer {

    public static void main(String ...args){


        Server server = new Server(65000);
        WebAppContext webAppContext = new WebAppContext();

        webAppContext.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        webAppContext.setResourceBase("src/main/webapp/");
        //webAppContext.setDisplayName("web");
        //webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        webAppContext.setConfigurationDiscovered(true);
        webAppContext.setParentLoaderPriority(true);

        server.setHandler(webAppContext);
        startMonitorThread();
        try {

            server.start();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   public static void startMonitorThread()
   {
       MonitorThread monitorThread = new MonitorThread();
       Common.monitorThread = monitorThread;
       monitorThread.start();
   }
}
