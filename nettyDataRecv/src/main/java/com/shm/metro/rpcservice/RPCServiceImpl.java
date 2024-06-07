package com.shm.metro.rpcservice;


import com.shm.metro.bean.ReloadResult;
import com.shm.metro.core.TCPServer;
import com.shm.metro.enums.NotifyType;
import com.shm.metro.protocol.BroadcastProtocol;
import com.shm.metro.util.CommonVar;
import com.shm.metro.util.HDFSUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2023/7/21.
 */
public class RPCServiceImpl implements RPCService {

    private BroadcastProtocol protocol;

    private TCPServer tcpServer;

    public RPCServiceImpl(BroadcastProtocol protocol,TCPServer tcpServer){
        this.protocol = protocol;
        this.tcpServer = tcpServer;
    }

    @Override
    public void updateJar(String jarPath, String requestId) {
        protocol.updateJarInfo(jarPath,requestId);
        try {
            fillResultSuccess(requestId);
            tcpServer.reloadJclBean();
            tcpServer.notify(NotifyType.CLASS_LOADER);
            HDFSUtil.setNewestJarToHDFS(jarPath);
        }catch (Exception e){
            filleResultFail(requestId, e);
        }

    }

    private void fillResultSuccess(String requestId) {
        ReloadResult reloadResult = new ReloadResult();
        reloadResult.setSuccess(true);
        //reloadResult.setErrorMsg(e.getMessage());
        reloadResult.setTraceId(requestId);
        reloadResult.setTimestamp(System.currentTimeMillis());
        CommonVar.RELOAD_REUSLT.put(requestId,reloadResult);
    }

    private void filleResultFail(String requestId, Exception e) {
        ReloadResult reloadResult = new ReloadResult();
        reloadResult.setSuccess(false);
        reloadResult.setErrorMsg(e.getMessage());
        reloadResult.setTraceId(requestId);
        reloadResult.setTimestamp(System.currentTimeMillis());
        CommonVar.RELOAD_REUSLT.put(requestId,reloadResult);
    }

    @Override
    public void updateTrainInfo(String requestId) {
        protocol.updateTrainDataInfo(requestId);
        try {
            fillResultSuccess(requestId);
            tcpServer.updateTrainData();
            tcpServer.notify(NotifyType.TRAIN_DATA);
        }catch (Exception e){
            filleResultFail(requestId, e);
        }
    }
}
