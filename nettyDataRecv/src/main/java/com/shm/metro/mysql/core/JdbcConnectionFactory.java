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

import com.shm.metro.base.ConnectionException;
import com.shm.metro.base.ConnectionFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <p>Title: JdbcConnectionFactory</p>
 * <p>Description: JDBC连接工厂</p>
 *
 * @since 2015年11月17日
 * @author Victor.Zxy
 * @see ConnectionFactory
 * @version 1.0
 */
class JdbcConnectionFactory implements ConnectionFactory<Connection> {

	/** serialVersionUID */
	private static final long serialVersionUID = 4941500146671191616L;

	/** driverClass */
	private final String driverClass;
	
	/** jdbcUrl */
	private final String jdbcUrl;
	
	/** username */
	private final String username;
	
	/** password */
	private final String password;

	/**
	 * <p>Title: loadDriver</p>
	 * <p>Description: 加载驱动</p>
	 */
	private void loadDriver() {

		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * <p>Title: JdbcConnectionFactory</p>
	 * <p>Description: 构造方法</p>
	 *
	 * @param properties JDBC参数
	 */
	public JdbcConnectionFactory(final Properties properties) {

		this.driverClass = properties.getProperty(JdbcConfig.DRIVER_CLASS_PROPERTY);
		if (driverClass == null)
			throw new ConnectionException("[" + JdbcConfig.DRIVER_CLASS_PROPERTY + "] is required !");
		
		this.jdbcUrl = properties.getProperty(JdbcConfig.JDBC_URL_PROPERTY);
		if (jdbcUrl == null)
			throw new ConnectionException("[" + JdbcConfig.JDBC_URL_PROPERTY + "] is required !");
		
		this.username = properties.getProperty(JdbcConfig.JDBC_USERNAME_PROPERTY);
		if (username == null)
			throw new ConnectionException("[" + JdbcConfig.JDBC_USERNAME_PROPERTY + "] is required !");
		
		this.password = properties.getProperty(JdbcConfig.JDBC_PASSWORD_PROPERTY);
		if (password == null)
			throw new ConnectionException("[" + JdbcConfig.JDBC_PASSWORD_PROPERTY + "] is required !");
		
		this.loadDriver();
	}
	
	/**
	 * <p>Title: JdbcConnectionFactory</p>
	 * <p>Description: 构造方法</p>
	 *
	 * @param driverClass 驱动类
	 * @param jdbcUrl 数据库URL
	 * @param username 数据库用户名
	 * @param password 数据密码
	 */
	public JdbcConnectionFactory(final String driverClass, final String jdbcUrl, final String username, final String password) {

		this.driverClass = driverClass;
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
		this.loadDriver();
	}
	
	@Override
	public PooledObject<Connection> makeObject() throws Exception {

		Connection connection = this.createConnection();
		
		return new DefaultPooledObject<Connection>(connection);
	}

	@Override
	public void destroyObject(PooledObject<Connection> p) throws Exception {
		
		Connection connection = p.getObject();

		if (connection != null)

			connection.close();
	}

	@Override
	public boolean validateObject(PooledObject<Connection> p) {

		Connection connection = p.getObject();
		
		if (connection != null)
			try {
				return ((!connection.isClosed()) && (connection.isValid(1)));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return false;
	}

	@Override
	public void activateObject(PooledObject<Connection> p) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void passivateObject(PooledObject<Connection> p) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Connection createConnection() throws Exception {

		Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
		
		return connection;
	}

}
