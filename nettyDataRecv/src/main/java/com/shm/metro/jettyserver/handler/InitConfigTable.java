package com.shm.metro.jettyserver.handler;

import com.alibaba.fastjson.JSONObject;
import com.shm.metro.function.Function;
import com.shm.metro.jettyserver.Common;
import com.shm.metro.jettyserver.bean.InitBean;
import com.shm.metro.jettyserver.util.MysqlUtil;
import com.shm.metro.jettyserver.util.PropertyUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Administrator on 2023/10/1.
 */
public class InitConfigTable extends HttpServlet{



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        final InitBean initBean = new InitBean();
        if(!PropertyUtil.isPropertiesFileExist()){
            initBean.setInit(false);
            initBean.setErrorCode(0);
            PropertyUtil.mkFile(PropertyUtil.configFilePath);
            resp.getWriter().write(JSONObject.toJSONString(initBean));

            return;
        }
        try {

            PropertyUtil.refresh();
            boolean isinit = Boolean.parseBoolean(PropertyUtil.getProperty("nettydatarecv.database.init"));

            if(isinit){
                initBean.setInit(true);

                String username = PropertyUtil.getProperty("nettydatarecv.database.username");

                String pwd = PropertyUtil.getProperty("nettydatarecv.database.pwd");

                String url = PropertyUtil.getProperty("nettydatarecv.database.url");

                String driver = PropertyUtil.getProperty("nettydatarecv.database.driver");

                final String dbName = PropertyUtil.getProperty("nettydatarecv.database.dbname");

                MysqlUtil.init(username,pwd,url,driver,dbName);

                String sql = "SELECT * FROM netty_config where is_work='true'";

                MysqlUtil.exec(sql, new Function<ResultSet>() {
                    @Override
                    public void call(ResultSet v) {
                        try {
                            String property = v.getString("property");
                            String value = v.getString("value");
                            initBean.getConfig().put(property,value);
                            if(property.equals("server_host")) {
                                Common.allServer = value;
                            }
                            if(property.equals("server_host_pwd")) {
                                Common.password = value;
                            }

                            if(property.equals("server_host_username")) {
                                Common.username = value;
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }

                });
                initBean.setErrorCode(0);
                resp.getWriter().write(JSONObject.toJSONString(initBean));
            }else{
                initBean.setInit(false);
                initBean.setErrorCode(0);
                resp.getWriter().write(JSONObject.toJSONString(initBean));
            }

        }catch (Exception e){
            initBean.setInit(false);
            initBean.setErrorCode(1);
            initBean.setMessage(e.getMessage());
            e.printStackTrace();
        }
    }


}
