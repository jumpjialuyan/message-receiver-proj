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
package com.shm.metro.mysql.core;

import com.shm.metro.base.ConnectionPool;
import com.shm.metro.base.PoolBase;
import com.shm.metro.base.PoolConfig;

import java.sql.Connection;
import java.util.Properties;


/**
 * <p>Title: JdbcConnectionPool</p>
 * <p>Description: Jdbc连接池</p>
 *
 * @since 2015年11月17日
 * @author Victor.Zxy
 * @see PoolBase
 * @see ConnectionPool
 * @version 1.0
 */
public class JdbcConnectionPool extends PoolBase<Connection> implements ConnectionPool<Connection> {

	/** serialVersionUID */
	private static final long serialVersionUID = 2743612676107943708L;

	/**
	 * <p>Title: JdbcConnectionPool</p>
	 * <p>Description: 默认构造方法</p>
	 */
	public JdbcConnectionPool() {

		this(JdbcConfig.DEFAULT_DRIVER_CLASS, JdbcConfig.DEFAULT_JDBC_URL, JdbcConfig.DEFAULT_JDBC_USERNAME, JdbcConfig.DEFAULT_JDBC_PASSWORD);
	}
	
	/**
	 * <p>Title: JdbcConnectionPool</p>
	 * <p>Description: 构造方法</p>
	 *
	 * @param properties JDBC参数
	 */
	public JdbcConnectionPool(final Properties properties) {
	
		this(new PoolConfig(), properties);
	}
	
	/**
	 * <p>Title: JdbcConnectionPool</p>
	 * <p>Description: 构造方法</p>
	 *
	 * @param driverClass 驱动类
	 * @param jdbcUrl 数据库URL
	 * @param username 数据库用户名
	 * @param password 数据密码
	 */
	public JdbcConnectionPool(final String driverClass, final String jdbcUrl, final String username, final String password) {

		this(new PoolConfig(), driverClass, jdbcUrl, username, password);
	}
	
	/**
	 * <p>Title: JdbcConnectionPool</p>
	 * <p>Description: 构造方法</p>
	 *
	 * @param poolConfig 池配置
	 * @param properties JDBC参数
	 */
	public JdbcConnectionPool(final PoolConfig poolConfig, final Properties properties) {
		
		super(poolConfig, new JdbcConnectionFactory(properties));
	}
	
	/**
	 * <p>Title: JdbcConnectionPool</p>
	 * <p>Description: 构造方法</p>
	 *
	 * @param poolConfig 池配置
	 * @param driverClass 驱动类
	 * @param jdbcUrl 数据库URL
	 * @param username 数据库用户名
	 * @param password 数据密码
	 */
	public JdbcConnectionPool(final PoolConfig poolConfig, final String driverClass, final String jdbcUrl, final String username, final String password) {

		super(poolConfig, new JdbcConnectionFactory(driverClass, jdbcUrl, username, password));
	}
	
	@Override
	public Connection getConnection() {
		
		return super.getResource();
	}

	@Override
	public void returnConnection(Connection conn) {
		
		super.returnResource(conn);
	}
	
	@Override
	public void invalidateConnection(Connection conn) {

		super.invalidateResource(conn);
	}

}
