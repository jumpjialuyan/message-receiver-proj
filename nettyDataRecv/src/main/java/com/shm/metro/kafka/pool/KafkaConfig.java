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

import com.shm.metro.util.BeanEncode;
import com.shm.metro.util.Util;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * <p>KafkaConfig</p>
 * <p>Kafka配置</p>
 *
 * @since 1.2.1
 * @author Victor
 */
public interface KafkaConfig {

	/** DEFAULT_BROKERS */
	public static final String DEFAULT_BROKERS = Util.getConfig().getProperty("kafka_broker_list");
	/** DEFAULT_TYPE */
	public static final String DEFAULT_TYPE = Util.getConfig().getProperty("producer.type");
	/** DEFAULT_ACKS */
	public static final String DEFAULT_ACKS = Util.getConfig().getProperty("acks");
	/**DEFAULT_KEY_SERIALIZER*/
	public static final String DEFAULT_KEY_SERIALIZER = Util.getConfig().getProperty("key.serializer");
	/** BATCH_NUMBER_PROPERTY */
	public static final String DEFAULT_VALUE_SERIALIZER = Util.getConfig().getProperty("value.serializer");

	/** BROKERS_LIST_PROPERTY */
	public static final String BROKERS_LIST_PROPERTY = "bootstrap.servers";
	/** PRODUCER_TYPE_PROPERTY */
	public static final String PRODUCER_TYPE_PROPERTY = "producer.type";
	/** REQUEST_ACKS_PROPERTY */
	public static final String REQUEST_ACKS_PROPERTY = "acks";
	/** KEY_SERIALIZER */
	public static final String KEY_SERIALIZER = "key.serializer";
	/** VALUE_SERIALIZER */
	public static final String VALUE_SERIALIZER = "value.serializer";


}
