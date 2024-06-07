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

import com.shm.metro.base.ConnectionPool;
import com.shm.metro.base.PoolBase;
import com.shm.metro.base.PoolConfig;
import com.shm.metro.protocol.DataDecode;
import org.apache.kafka.clients.producer.KafkaProducer;


/**
 * <p>Title: KafkaConnectionPool</p>
 * <p>Description: Kafka连接池</p>
 *
 * @since 2015年9月19日
 * @author Victor
 * @see PoolBase
 * @see ConnectionPool
 * @version 1.0
 */
public class KafkaConnectionPool extends PoolBase<KafkaProducer<String, DataDecode>> implements ConnectionPool<KafkaProducer<String, DataDecode>> {

	/** serialVersionUID */
	private static final long serialVersionUID = -1506435964498488591L;

	/**
	 * <p>Title: KafkaConnectionPool</p>
	 * <p>Description: 默认构造方法</p>
	 *
	 */
	public KafkaConnectionPool() {

		this(KafkaConfig.DEFAULT_BROKERS);
	}
	
	/**
	 * <p>Title: KafkaConnectionPool</p>
	 * <p>Description: 构造方法</p>
	 *
	 * @param brokers broker列表
	 */
	public KafkaConnectionPool(final String brokers) {

		this(new PoolConfig(), brokers);
	}

	/**
	 * <p>Title: KafkaConnectionPool</p>
	 * <p>Description: 构造方法</p>
	 *
	 * @param poolConfig 池配置
	 * @param brokers broker列表
	 */
	public KafkaConnectionPool(final PoolConfig poolConfig, final String brokers) {

		this(poolConfig, brokers, KafkaConfig.DEFAULT_TYPE, KafkaConfig.DEFAULT_ACKS, KafkaConfig.DEFAULT_KEY_SERIALIZER, KafkaConfig.DEFAULT_VALUE_SERIALIZER);
	}
	
	/**
	 * <p>Title: KafkaConnectionPool</p>
	 * <p>Description: 构造方法</p>
	 *
	 * @param poolConfig 池配置
	 * @param brokers broker列表
	 * @param type 生产者类型
	 */
	public KafkaConnectionPool(final PoolConfig poolConfig, final String brokers, final String type) {

		this(poolConfig, brokers, type, KafkaConfig.DEFAULT_ACKS, KafkaConfig.DEFAULT_KEY_SERIALIZER, KafkaConfig.DEFAULT_VALUE_SERIALIZER);
	}

	
	/**
	 * <p>Title: KafkaConnectionPool</p>
	 * <p>Description: 构造方法</p>
	 *
	 * @param poolConfig 池配置
	 * @param brokers broker列表
	 * @param type 生产者类型
	 * @param acks 确认类型
	 * @param keyseri key序列化类型
	 * @param valueseri value序列化类型
	 */
	public KafkaConnectionPool(final PoolConfig poolConfig, final String brokers, final String type, final String acks, final String keyseri, final String valueseri) {

		super(poolConfig, new KafkaConnectionFactory(brokers, type, acks, keyseri, valueseri));
	}
	
	@Override
	public KafkaProducer<String, DataDecode> getConnection() {

		return super.getResource();
	}

	@Override
	public void returnConnection(KafkaProducer<String, DataDecode> conn) {
		
		super.returnResource(conn);
	}

	@Override
	public void invalidateConnection(KafkaProducer<String, DataDecode> conn) {

		super.invalidateResource(conn);
	}
}
