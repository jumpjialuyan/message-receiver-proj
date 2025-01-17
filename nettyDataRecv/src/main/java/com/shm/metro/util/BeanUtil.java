 package com.shm.metro.util;

 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.ObjectOutputStream;

 public class BeanUtil
 {
   public static byte[] toByteArray(Object obj)
   {
     byte[] bytes = null;
     ByteArrayOutputStream bos = new ByteArrayOutputStream();
     try {
       ObjectOutputStream oos = new ObjectOutputStream(bos);
       oos.writeObject(obj);
       oos.flush();
       bytes = bos.toByteArray();
       oos.close();
       bos.close();
     } catch (IOException ex) {
       ex.printStackTrace();
     }
     return bytes;
   }

   public static Object toObject(byte[] bytes) {
     Object obj = null;
     try {
       java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(bytes);
       ObjectInputStream ois = new ObjectInputStream(bis);
       obj = ois.readObject();
       ois.close();
       bis.close();
     } catch (IOException ex) {
       ex.printStackTrace();
     } catch (ClassNotFoundException ex) {
       ex.printStackTrace();
     }
     return obj;
   }
 }

