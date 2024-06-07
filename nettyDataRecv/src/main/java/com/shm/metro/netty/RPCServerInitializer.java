package com.shm.metro.netty;

import com.shm.metro.core.TCPServer;
import com.shm.metro.protocol.*;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.log4j.Logger;


/**
 * Created by Administrator on 2023/7/4.
 */
public class RPCServerInitializer extends ChannelInitializer<SocketChannel> {

    private Logger logger = Logger.getLogger(RPCServerInitializer.class);

    private BroadcastProtocol broadcast;

    private TCPServer tcpServer;

    public RPCServerInitializer(BroadcastProtocol broadcast,TCPServer tcpServer)
    {
        this.tcpServer = tcpServer;
        this.broadcast = broadcast;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 0))
                .addLast(new RpcDecoder(RpcRequest.class))
                .addLast(new RpcEncoder(RpcResponse.class))
                .addLast(new RPCServerHandler(broadcast,tcpServer));
        if(logger.isInfoEnabled()) {
            logger.info(socketChannel.remoteAddress() + " 连接上\n");
        }
    }
}
