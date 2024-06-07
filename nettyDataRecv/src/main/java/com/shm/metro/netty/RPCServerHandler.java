package com.shm.metro.netty;


import com.shm.metro.bean.ReloadResult;
import com.shm.metro.core.TCPServer;
import com.shm.metro.protocol.BroadcastProtocol;
import com.shm.metro.protocol.RpcRequest;
import com.shm.metro.protocol.RpcResponse;
import com.shm.metro.rpcservice.RPCServiceImpl;
import com.shm.metro.util.CommonVar;
import com.shm.metro.util.Util;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2023/7/4.
 */
public class RPCServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private BroadcastProtocol broadcast;

    private Object serviceBean = null;

    public RPCServerHandler(BroadcastProtocol broadcast, TCPServer tcpServer) {
        this.broadcast = broadcast;
        serviceBean = new RPCServiceImpl(this.broadcast,tcpServer);
    }

    private Logger logger = Logger.getLogger(RPCServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, final RpcRequest request) throws Exception {
        if(logger.isInfoEnabled()) {
            logger.info("Receive request " + request.getRequestId());
        }
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        try {
            handle(request);

            ReloadResult reloadResult = CommonVar.RELOAD_REUSLT.get(request.getRequestId());

            if(reloadResult.getSuccess()){
                response.setError(reloadResult.getErrorMsg());
                List<ReloadResult> reloadResultList = new ArrayList<>();
                reloadResultList.add(reloadResult);
                response.setResult(reloadResultList);
            }

            if (logger.isInfoEnabled()) {
                logger.info("Receive request info : " + broadcast);
            }

            CommonVar.RELOAD_REUSLT.remove(request.getRequestId());

        } catch (Throwable t) {
            ReloadResult reloadResult = new ReloadResult();
            reloadResult.setSuccess(false);
            reloadResult.setErrorMsg(t.getMessage());
            reloadResult.setTraceId(request.getRequestId());
            reloadResult.setTimestamp(System.currentTimeMillis());
            CommonVar.RELOAD_REUSLT.put(request.getRequestId(),reloadResult);
            List<ReloadResult> reloadResultList = new ArrayList<>();
            reloadResultList.add(reloadResult);
            response.setResult(reloadResultList);
            response.setError(t.toString());
            CommonVar.RELOAD_REUSLT.get(request.getRequestId());
            logger.error("RPC Server handle request error");
        }
        channelHandlerContext.writeAndFlush(response).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(logger.isInfoEnabled()) {
                    logger.info("Send response for request " + request.getRequestId());
                }
            }
        });
        // = context.sparkContext().broadcast(String.valueOf(System.currentTimeMillis()));
    }

    private Object handle(RpcRequest request) throws Throwable {
        String className = request.getClassName();
        ///Object serviceBean = new RPCServiceImpl();

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParameters();

        /*for (int i = 0; i < parameterTypes.length; ++i) {
            System.out.println(parameterTypes[i].getName());
        }
        for (int i = 0; i < parameters.length; ++i) {
            System.out.println(parameters[i].toString());
        }*/

        // JDK reflect
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);

        // Cglib reflect
        /*FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);*/
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        logger.error("SimpleChatClient:" + incoming.remoteAddress() + "异常");
        cause.printStackTrace();
        ctx.close();
    }
}




















