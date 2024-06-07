package com.shm.metro.jettyserver.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2023/10/1.
 */
public class InitBean extends BaseResultBean{
    private boolean isInit;

    private Map<String,String> config = new HashMap<>();

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}
