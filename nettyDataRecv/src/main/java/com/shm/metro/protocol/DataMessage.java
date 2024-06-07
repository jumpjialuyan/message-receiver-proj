 package com.shm.metro.protocol;

 import java.io.Serializable;


 public class DataMessage
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private String TrainID;
   private byte[] buffer = null;

   public String getTrainID() {
     return TrainID;
   }

   public void setTrainID(String trainID) {
     TrainID = trainID;
   }

   public byte[] getBuffer() { return this.buffer; }

   public void setBuffer(byte[] buffer) {
     this.buffer = new byte[buffer.length];
     this.buffer = buffer;
   }
 }
