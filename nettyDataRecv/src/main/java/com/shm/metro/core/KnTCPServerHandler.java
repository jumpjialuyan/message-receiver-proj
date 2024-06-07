 package com.shm.metro.core;

 import com.alibaba.fastjson.JSONObject;
 import com.shm.metro.kafka.pool.KafkaConfig;
 import com.shm.metro.util.CrcUtil;
 import com.shm.metro.util.Util;
 import io.netty.buffer.ByteBuf;
 import io.netty.channel.ChannelHandlerContext;
 import io.netty.channel.SimpleChannelInboundHandler;
 import org.apache.kafka.clients.producer.KafkaProducer;
 import org.apache.kafka.clients.producer.ProducerRecord;
 import org.apache.log4j.Logger;

 import java.util.Properties;

 public class KnTCPServerHandler extends SimpleChannelInboundHandler<JSONObject> {

     static Logger logger = Logger.getLogger("kangNiLogger");

     public static String topic = Util.getConfig().getProperty("kangni_kafka_topic");

     protected static final char[] hexArray = "0123456789abcdef".toCharArray();

     static KafkaProducer<String, String> producer = null;
     private synchronized static void initKnProducer(){
         Properties props = new Properties();
         props.put("bootstrap.servers", KafkaConfig.DEFAULT_BROKERS);
         props.put("acks", KafkaConfig.DEFAULT_ACKS);
         props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
         props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
         producer = new KafkaProducer<>(props);
     }

     public static String bytesToHexString(byte[] bytes) {
         char[] hexChars = new char[bytes.length * 2];
         for (int j = 0; j < bytes.length; j++) {
             int v = bytes[j] & 0xFF;
             hexChars[(j * 2)] = hexArray[(v >>> 4)];
             hexChars[(j * 2 + 1)] = hexArray[(v & 0xF)];
         }
         return new String(hexChars);
     }

     public void writeKafka(JSONObject msgJson) {
         byte[] packageByte = msgJson.getBytes("packageByte");
         try {
             if (logger.isInfoEnabled()) {
                 logger.info("begin write data " + msgJson.get("trainNo") + " to kafka");
             }
             if (msgJson.get("trainNo") != null) {
                 ProducerRecord<String, String> data = new ProducerRecord(topic, bytesToHexString(packageByte));
                 if(producer == null){
                     initKnProducer();
                 }
                 if(producer != null) {
                     producer.send(data);
                 }else
                 {
                     logger.error("获取kafka连接失败！");
                     throw new Exception("获取kakfa连接失败");
                 }

             } else {
                 logger.error("收到的报文，但是车厢信息不存在！");
             }
             if (logger.isInfoEnabled()) {
                 logger.info("end write data " + msgJson.get("trainNo") + " to kafka");
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
         cause.printStackTrace();
     }

     @Override
     protected void channelRead0(ChannelHandlerContext ctx, JSONObject msgJson) throws Exception {
         //存kafka
         if(msgJson.get("trainNo") != null){
             byte[] responseByte = msgJson.getBytes("responseByte");
             byte[] packageByte = msgJson.getBytes("packageByte");
             //判断crc校验是否通过
             if(!CrcUtil.isPassCRC(packageByte, 2)){
                 ByteBuf buf = ctx.alloc().buffer(1);
                 buf.writeInt(0);
                 ctx.writeAndFlush(buf);
                 logger.error("收到不合法报文！");
                 return;
             }
             //存kafka
             writeKafka(msgJson);
             ByteBuf buf = ctx.alloc().buffer(responseByte.length);
             buf.writeBytes(responseByte);
             if(logger.isInfoEnabled()) {
                 logger.info("康尼数据");
                 logger.info("返回数据：" + bytesToHexString(responseByte));
             }
             ctx.writeAndFlush(buf);
         }else{
             ByteBuf buf = ctx.alloc().buffer(1);
             buf.writeInt(0);
             ctx.writeAndFlush(buf);
         }
     }

 }

