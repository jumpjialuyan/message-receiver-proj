 package com.shm.metro.util;

 import com.shm.metro.protocol.DataDecode;
 import org.apache.kafka.common.serialization.Serializer;

 import java.util.Map;


 public class BeanEncode
   implements Serializer<DataDecode>
 {
   public void close() {}

   public byte[] serialize(String arg0, DataDecode arg1)
   {
     return BeanUtil.toByteArray(arg1);
   }

   public void configure(Map arg0, boolean arg1) {}
 }
