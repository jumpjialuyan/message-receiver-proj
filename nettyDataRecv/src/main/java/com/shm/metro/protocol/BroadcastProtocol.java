package com.shm.metro.protocol;

import java.io.Serializable;

/**
 * Created by Administrator on 2023/7/19.
 */
public class BroadcastProtocol implements scala.Serializable,Serializable{


    private String traceId= "";

    private boolean updateJar=false;
    private String jarPath;
    private long updateJarKey;

    private boolean updateTrainData=false;
    private long updateTrainDataKey;

    private boolean updateFaultData=false;
    private long updateFaultDataKey;

    public BroadcastProtocol(){}

    public synchronized void reset(){
        this.updateJar = false;
        this.updateFaultData = false;
        this.updateTrainData = false;
    }

    public BroadcastProtocol(BroadcastProtocol protocol){
        this.updateJar = protocol.isUpdateJar();
        this.updateJarKey = protocol.getUpdateJarKey();
        this.jarPath = protocol.getJarPath();
        this.updateTrainData = protocol.isUpdateTrainData();
        this.updateTrainDataKey = protocol.getUpdateTrainDataKey();
        this.updateFaultData = protocol.isUpdateFaultData();
        this.updateFaultDataKey = protocol.getUpdateFaultDataKey();
        this.traceId = protocol.getTraceId();
    }


    public synchronized String getTraceId() {
        return traceId;
    }

    public synchronized void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public void updateJarInfo(String jarPath,String requestId){
        reset();
        this.setUpdateJar(true);
        this.setUpdateJarKey(System.currentTimeMillis());
        this.setJarPath(jarPath);
        this.setTraceId(requestId);
    }

    public void updateFaultDataInfo(String requestId){
        reset();
        this.setUpdateFaultData(true);
        this.setUpdateFaultDataKey(System.currentTimeMillis());
        this.setTraceId(requestId);
    }

    public void updateTrainDataInfo(String requestId){
        reset();
        this.setUpdateTrainData(true);
        this.setUpdateTrainDataKey(System.currentTimeMillis());
        this.setTraceId(requestId);
    }

    public synchronized boolean isUpdateFaultData() {
        return updateFaultData;
    }

    public synchronized void setUpdateFaultData(boolean updateFaultData) {
        this.updateFaultData = updateFaultData;
    }

    public synchronized long getUpdateFaultDataKey() {
        return updateFaultDataKey;
    }

    public synchronized void setUpdateFaultDataKey(long updateFaultDataKey) {
        this.updateFaultDataKey = updateFaultDataKey;
    }

    public synchronized long getUpdateTrainDataKey() {
        return updateTrainDataKey;
    }

    public synchronized void setUpdateTrainDataKey(long updateTrainDataKey) {
        this.updateTrainDataKey = updateTrainDataKey;
    }

    public synchronized boolean isUpdateJar() {
        return updateJar;
    }

    public synchronized void setUpdateJar(boolean updateJar) {
        this.updateJar = updateJar;
    }

    public synchronized boolean isUpdateTrainData() {
        return updateTrainData;
    }

    public synchronized void setUpdateTrainData(boolean updateTrainData) {
        this.updateTrainData = updateTrainData;
    }

    public synchronized long getUpdateJarKey() {
        return updateJarKey;
    }

    public synchronized void setUpdateJarKey(long updateJarKey) {
        this.updateJarKey = updateJarKey;
    }

    public synchronized String getJarPath() {
        return jarPath;
    }

    public synchronized void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    @Override
    public String toString() {
        return "BroadcastProtocol{" +
                "traceId='" + traceId + '\'' +
                ", updateJar=" + updateJar +
                ", jarPath='" + jarPath + '\'' +
                ", updateJarKey=" + updateJarKey +
                ", updateTrainData=" + updateTrainData +
                ", updateTrainDataKey=" + updateTrainDataKey +
                ", updateFaultData=" + updateFaultData +
                ", updateFaultDataKey=" + updateFaultDataKey +
                '}';
    }
}

