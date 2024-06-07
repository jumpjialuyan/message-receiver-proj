 package com.shm.metro.base;

 import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

 import java.io.Serializable;


 public class PoolConfig
   extends GenericObjectPoolConfig
   implements Serializable
 {
   private static final long serialVersionUID = -2414567557372345057L;
   public static final boolean DEFAULT_TEST_WHILE_IDLE = true;
   public static final long DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 60000L;
   public static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = 30000L;
   public static final int DEFAULT_NUM_TESTS_PER_EVICTION_RUN = -1;

   public PoolConfig()
   {
     setTestWhileIdle(true);
     setMinEvictableIdleTimeMillis(60000L);
     setTimeBetweenEvictionRunsMillis(30000L);
     setNumTestsPerEvictionRun(-1);
   }
 }

