package com.shm.metro.rpcservice;


import com.shm.metro.protocol.BroadcastProtocol;

import java.util.List;

/**
 * Created by Administrator on 2023/7/21.
 */
public interface RPCService {
    /**
     * 更新jar包
     * @param jarPath
     * @param requestId
     */
    void updateJar(String jarPath, String requestId);

    /**
     * 更新主数据信息
     * @param requestId
     */
    void updateTrainInfo(String requestId);

}
