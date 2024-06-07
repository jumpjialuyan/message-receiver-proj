package com.shm.metro.jettyserver.util;

import com.shm.metro.function.Function;
import com.shm.metro.mysql.core.JdbcConnectionPool;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2023/10/1.
 */
public class MysqlUtil {

    private static JdbcConnectionPool pool;
    public static AtomicBoolean isInit = new AtomicBoolean(false);

    public static String mysqlUsername;

    public static String mysqlPWD;

    public static String mysqlUrl;

    public static String mysqlDriver;

    public static String mysqldbName;

    public static void init(String username,String pwd,String url,String driver,String dbName){
        mysqlUsername = username;
        mysqlPWD = pwd;
        mysqlUrl = url;
        mysqlDriver = driver;
        mysqldbName = dbName;
        pool = new JdbcConnectionPool(mysqlDriver,mysqlUrl+ File.separator+mysqldbName,mysqlUsername,mysqlPWD);
        isInit.set(true);
    }

    public static void init(){
        pool = new JdbcConnectionPool(mysqlDriver,mysqlUrl+ File.separator+mysqldbName,mysqlUsername,mysqlPWD);
        isInit.set(true);
    }

    private synchronized static Connection getConnection(){
        return pool.getConnection();
    }

    private synchronized static void returnConnection(Connection connection){
        pool.returnConnection(connection);
    }


    public static void exec(String sql, Function<ResultSet> function){

        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                function.call(resultSet);
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection != null) {
                returnConnection(connection);
            }
        }

    }

    public static void  exec(String sql) throws SQLException{
        Connection connection = getConnection();
        boolean resultSet = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new SQLException(e);
        }finally {
            if(connection != null) {
                returnConnection(connection);
            }
        }
    }



}
