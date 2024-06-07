package com.shm.metro.jettyserver.bean;

/**
 * Created by Administrator on 2023/11/10.
 */
public class UploadProcessBean {
    private long totalSize;
    private long uploadedSize;
    private Boolean hasError;
    private String errorMsg;
    private double uploadPercent;

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getUploadedSize() {
        return uploadedSize;
    }

    public void setUploadedSize(long uploadedSize) {
        this.uploadedSize = uploadedSize;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public double getUploadPercent() {
        if(totalSize==0){
            return 0;
        }else{
            return (double) uploadedSize/totalSize;
        }
        //return uploadPercent;
    }

    public void setUploadPercent(double uploadPercent) {
        this.uploadPercent = uploadPercent;
    }
}
