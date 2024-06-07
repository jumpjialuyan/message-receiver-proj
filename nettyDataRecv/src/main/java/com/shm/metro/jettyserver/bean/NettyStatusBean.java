package com.shm.metro.jettyserver.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2023/10/2.
 */
public class NettyStatusBean extends BaseResultBean {
    private List<String> onlineServer;

    private List<String> allServer;

    private Map<String,Boolean> status = new HashMap<>();

    public List<String> getAllServer() {
        return allServer;
    }

    public void setAllServer(List<String> allServer) {
        this.allServer = allServer;
    }

    public List<String> getOnlineServer() {
        return onlineServer;
    }

    public void setOnlineServer(List<String> onlineServer) {
        this.onlineServer = onlineServer;
    }

    public Map<String, Boolean> getStatus() {
        return status;
    }

    public void setStatus(Map<String, Boolean> status) {
        this.status = status;
    }
}
