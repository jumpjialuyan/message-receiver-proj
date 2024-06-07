/*
 * Copyright 2015-2016 Dark Phoenixs (Open-Source Organization).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shm.metro.kafka.pool;

import java.util.Properties;

import com.shm.metro.base.ConnectionFactory;
import com.shm.metro.protocol.DataDecode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.kafka.clients.producer.KafkaProducer;


/**
 * <p>Title: KafkaConnectionFactory</p>
 * <p>Description: Kafka连接工厂</p>
 *
 * @since 2015年9月19日
 * @author Victor
 * @see ConnectionFactory
 * @version 1.0
 */
class KafkaConnectionFactory implements ConnectionFactory<KafkaProducer<String, DataDecode>> {

	/** serialVersionUID */
	private static final long serialVersionUID = 8271607366818512399L;

	/** config */
	private final Properties config;
	

	/**
	 * <p>Title: KafkaConnectionFactory</p>
	 * <p>Description: 构造方法</p>
	 *
	 * @param brokers broker列表
	 * @param type 生产者类型
	 * @param acks 确认类型
	 * @param keyseri key序列化类型
	 * @param valueseri value序列化类型
	 */
	public KafkaConnectionFactory(final String brokers, final String type, final String acks, final String keyseri, final String valueseri) {
		
		Properties props = new Properties();
		props.setProperty(KafkaConfig.BROKERS_LIST_PROPERTY, brokers);
		//props.setProperty(KafkaConfig.PRODUCER_TYPE_PROPERTY, type);
		props.setProperty(KafkaConfig.REQUEST_ACKS_PROPERTY, acks);
		props.setProperty(KafkaConfig.KEY_SERIALIZER, keyseri);
		props.setProperty(KafkaConfig.VALUE_SERIALIZER, valueseri);
		this.config = props;
	}

	
	@Override
	public PooledObject<KafkaProducer<String, DataDecode>> makeObject() throws Exception {

		KafkaProducer<String, DataDecode> producer = this.createConnection();
		
		return new DefaultPooledObject<KafkaProducer<String, DataDecode>>(producer);
	}

	@Override
	public void destroyObject(PooledObject<KafkaProducer<String, DataDecode>> p)
			throws Exception {

		KafkaProducer<String, DataDecode> producer = p.getObject();
		
		if (null != producer)
			
			producer.close();
	}

	@Override
	public boolean validateObject(PooledObject<KafkaProducer<String, DataDecode>> p) {

		KafkaProducer<String, DataDecode> producer = p.getObject();
		
		return (null != producer);
	}

	@Override
	public void activateObject(PooledObject<KafkaProducer<String, DataDecode>> p)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void passivateObject(PooledObject<KafkaProducer<String, DataDecode>> p)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public KafkaProducer<String, DataDecode> createConnection() throws Exception {

		KafkaProducer<String, DataDecode> producer = new KafkaProducer<String, DataDecode>(config);

		return producer;
	}
}
