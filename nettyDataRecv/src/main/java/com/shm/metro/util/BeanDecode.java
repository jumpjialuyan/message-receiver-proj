 package com.shm.metro.util;

 import kafka.serializer.Decoder;
 import kafka.utils.VerifiableProperties;


 public class BeanDecode<T>
   implements Decoder<T>
 {
   public BeanDecode(VerifiableProperties props) {}

   public T fromBytes(byte[] bytes)
   {
     return (T) BeanUtil.toObject(bytes);
   }
 }

