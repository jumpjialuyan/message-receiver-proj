package com.shm.metro.jettyserver.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2023/10/6.
 */
public class DispactchInfoBean extends BaseResultBean {

    private String cmd;

    private UploadProcessBean uploadProcessBean;

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public UploadProcessBean getUploadProcessBean() {
        return uploadProcessBean;
    }

    public void setUploadProcessBean(UploadProcessBean uploadProcessBean) {
        this.uploadProcessBean = uploadProcessBean;
    }

    private Map<String,Boolean> result = new HashMap<>();

    public Map<String, Boolean> getResult() {
        return result;
    }

    public void setResult(Map<String, Boolean> result) {
        this.result = result;
    }
}
