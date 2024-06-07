 package com.shm.metro.protocol;

 import com.shm.metro.bean.TrainData;

 import java.io.Serializable;
 import java.util.Arrays;
 import java.util.Date;
 import java.util.Map;

 public class DataDecode
   implements Serializable {
     private static final long serialVersionUID = 1L;
     private TrainData trainData;
     private long timestamp;
     private byte[] responseData;
     private boolean CRCResult;
     private byte[] buffer;
     private Map head;
     private String version;

     private byte msgType;

     public DataDecode() {
     }

     public DataDecode(TrainData trainData, long timestamp, byte[] buffer, byte[] responseData_0, byte CrcResult, byte msgType, Map head,String version) {
         this.trainData = trainData;
         this.timestamp = timestamp;
         this.buffer = buffer;
         this.msgType = msgType;
         this.CRCResult = (CrcResult == 0x01 || CrcResult == 0x11) ? true : false;
         this.responseData = new byte[responseData_0.length];
         this.head = head;
         this.version = version;
         System.arraycopy(responseData_0, 0, this.responseData, 0, responseData_0.length);
     }

     public byte getMsgType() {
         return msgType;
     }

     public void setMsgType(byte msgType) {
         this.msgType = msgType;
     }

     public boolean isCRCResult() {
         return CRCResult;
     }

     public void setCRCResult(boolean CRCResult) {
         this.CRCResult = CRCResult;
     }

     public byte[] getResponseData() {
         return responseData;
     }

     public void setResponseData(byte[] responseData) {
         this.responseData = responseData;
     }

     public void setTimestamp(long timestamp) {
         this.timestamp = timestamp;
     }

     public long getTimestamp() {
         return timestamp;
     }

     public TrainData getTrainData() {
         return trainData;
     }

     public void setTrainData(TrainData trainData) {
         this.trainData = trainData;
     }

     public byte[] getBuffer() {
         return buffer;
     }

     public void setBuffer(byte[] buffer) {
         this.buffer = buffer;
     }

     public Map getHead() {
         return head;
     }

     public void setHead(Map head) {
         this.head = head;
     }

     public String getVersion() {
         return version;
     }

     public void setVersion(String version) {
         this.version = version;
     }

     @Override
     public String toString() {
         return "DataDecode{" +
                 "trainData=" + trainData +
                 ", timestamp=" + timestamp +
                 ", buffer=" + Arrays.toString(buffer) +
                 '}';
     }
 }
