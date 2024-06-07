package com.shm.metro.kafka;/**
 * Created by Jerry on 2023/12/26.
 */

import com.shm.metro.kafka.pool.KafkaConfig;
import com.shm.metro.kafka.pool.KafkaConnectionPool;
import com.shm.metro.protocol.DataDecode;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;
import java.util.concurrent.Semaphore;

/**
 * @author Jerry
 * @create 2023-12-26 上午1:42
 **/

public class KafkaClient {

    private static KafkaConnectionPool kafkaConnectionPool = new KafkaConnectionPool();

   /* static Properties props = new Properties();
    static KafkaProducer<String, DataDecode> producer;

    static {
        props.setProperty(KafkaConfig.BROKERS_LIST_PROPERTY, KafkaConfig.DEFAULT_BROKERS);
        //props.setProperty(KafkaConfig.PRODUCER_TYPE_PROPERTY, type);
        props.setProperty(KafkaConfig.REQUEST_ACKS_PROPERTY, KafkaConfig.DEFAULT_ACKS);
        props.setProperty(KafkaConfig.KEY_SERIALIZER, KafkaConfig.DEFAULT_KEY_SERIALIZER);
        props.setProperty(KafkaConfig.VALUE_SERIALIZER, KafkaConfig.DEFAULT_VALUE_SERIALIZER);
        producer = new KafkaProducer<String, DataDecode>(props);
    }*/

    static final Semaphore semp = new Semaphore(kafkaConnectionPool.getMaxTotal());

    public static void main(String[] args){

        System.out.println(kafkaConnectionPool.getMaxWaitMillis());

        for(int i =0 ;i < 10;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    KafkaProducer<String, DataDecode> connection = getProducer();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    returnProducerConnection(connection);
                    System.out.println(Thread.currentThread().getName());
                }
            }).start();
        }
        System.out.println(kafkaConnectionPool.getMaxTotal());
    }


    public static KafkaProducer<String, DataDecode> getProducer() {
        try {
            semp.acquire();
            return kafkaConnectionPool.getConnection();
        }catch (InterruptedException e){
            e.printStackTrace();
            semp.release();
        }
        return null;
    }


    public static void returnProducerConnection(KafkaProducer producer) {
        kafkaConnectionPool.returnConnection(producer);
        semp.release();
    }

}
