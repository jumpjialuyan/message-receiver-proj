package com.shm.metro.util;

import com.shm.metro.function.Function;
import com.shm.metro.mysql.core.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2023/8/29.
 */
public class MySqlUtil {
    private static JdbcConnectionPool pool = new JdbcConnectionPool();

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

    public static boolean exec(String sql){
        Connection connection = getConnection();
        boolean resultSet = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(connection != null) {
                returnConnection(connection);
            }
        }
        return resultSet;
    }

}
