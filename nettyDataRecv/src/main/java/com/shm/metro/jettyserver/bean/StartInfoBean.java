package com.shm.metro.jettyserver.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2023/10/6.
 */
public class StartInfoBean extends BaseResultBean {
    private Map<String,String> result = new HashMap<>();

    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }
}
