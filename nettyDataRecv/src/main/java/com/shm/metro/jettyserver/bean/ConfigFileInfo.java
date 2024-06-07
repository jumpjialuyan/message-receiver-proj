package com.shm.metro.jettyserver.bean;

/**
 * Created by Administrator on 2023/11/9.
 */
public class ConfigFileInfo {
    private Long totalSize;
    private Long uploadedSize;
    private String server;
    private Boolean isUploaded = false;

    public synchronized Boolean getUploaded() {
        return isUploaded;
    }
    public synchronized void setUploaded(Boolean uploaded) {
        isUploaded = uploaded;
    }
    public synchronized Long getTotalSize() {
        return totalSize;
    }

    public synchronized void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public synchronized Long getUploadedSize() {
        return uploadedSize;
    }

    public synchronized void setUploadedSize(Long uploadedSize) {
        this.uploadedSize = uploadedSize;
    }

    public synchronized String getServer() {
        return server;
    }

    public synchronized void setServer(String server) {
        this.server = server;
    }
}
