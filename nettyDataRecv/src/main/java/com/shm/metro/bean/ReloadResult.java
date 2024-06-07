package com.shm.metro.bean;

/**
 * Created by Jerry on 2023/12/4.
 */
public class ReloadResult {
    boolean success;
    String traceId;
    Long timestamp;
    String executorId;
    String  errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ReloadResult{" +
                "success=" + success +
                ", traceId='" + traceId + '\'' +
                ", timestamp=" + timestamp +
                ", executorId='" + executorId + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
