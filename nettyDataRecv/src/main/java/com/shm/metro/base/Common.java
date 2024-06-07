 package com.shm.metro.base;

 public class Common
 {
   public static final String PROTOCOL_SCHEMA = "ws://";
   public static final int WEBSOCKET_SERVER_PORT = 8888;
   public static final String WEBSOCKET_SERVER_URI = "/websocket/spark";
   public static final String GPS_TAG = "gps";
   public static final String MYSQL_DRIVER_NAME = "com.mysql.jdbc.Driver";
   public static int maxTextMessageSize = 1048576;
   public static int maxBinaryMessageSize = 65536;
   public static long idleTimeout = 7200000L;
   public static long asyncWriteTimeout = 300000L;

   public static String WEBSOCKET_SERVER_IP = "10.12.18.38";

   public static final String MYSQL_URL = "jdbc:mysql://10.12.18.181:3306/air_condition?useUnicode=true&characterEncoding=utf8";
   public static final String MYSQL_URL2 = "jdbc:mysql://10.12.18.181:3306/csrdb1?useUnicode=true&characterEncoding=utf8";
   public static final String DB_USERNAME = "root";
   public static final String DB_PASSWORD = "123456";
   public static final String QUORUM = "mdp1,mdp2,mdp3";
   public static final String MASTER = "mdp4:60000";
   public static final String CLIENTPORT = "2181";

   public static String getWebSocketServiceUrl()
   {
     return "ws://" + WEBSOCKET_SERVER_IP + ":" + 8888 + "/websocket/spark";
   }

   public static void main(String[] args) {
     System.out.println(getWebSocketServiceUrl());
   }
 }
