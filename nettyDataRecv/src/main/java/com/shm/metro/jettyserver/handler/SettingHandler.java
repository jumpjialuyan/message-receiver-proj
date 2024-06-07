package com.shm.metro.jettyserver.handler;

import com.alibaba.fastjson.JSONObject;
import com.shm.metro.jettyserver.Common;
import com.shm.metro.jettyserver.bean.BaseResultBean;
import com.shm.metro.jettyserver.util.MysqlUtil;
import com.shm.metro.jettyserver.util.PropertyUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2023/10/1.
 */
public class SettingHandler extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        boolean isFirst = Boolean.parseBoolean(req.getParameter("isFirst"));
        String mysql_username = req.getParameter("mysql_username");
        String mysql_pwd = req.getParameter("mysql_pwd");
        String mysql_driver = req.getParameter("mysql_driver");
        String mysql_URL = req.getParameter("mysql_URL");
        String mysql_database_name = req.getParameter("mysql_database_name");

        String errorMsg = "";
        int errorCode = 0;

        if(isFirst){
            try {

                PropertyUtil.reWriteProperty("nettydatarecv.database.username", mysql_username);
                PropertyUtil.reWriteProperty("nettydatarecv.database.pwd", mysql_pwd);
                PropertyUtil.reWriteProperty("nettydatarecv.database.url", mysql_URL);
                PropertyUtil.reWriteProperty("nettydatarecv.database.driver", mysql_driver);
                PropertyUtil.reWriteProperty("nettydatarecv.database.dbname", mysql_database_name);
                PropertyUtil.reWriteProperty("nettydatarecv.database.init", "true");
                PropertyUtil.flush();

                resp.sendRedirect("/servlet/initConfig");

            }catch (Exception e){
                BaseResultBean baseResultBean = new BaseResultBean();
                baseResultBean.setErrorCode(0);
                baseResultBean.setMessage("写入文件错误："+e.getMessage());
                resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
            }



        }else{

            String add = req.getParameter("add");
            String update = req.getParameter("update");
            String delete = req.getParameter("delete");

            JSONObject addJson = JSONObject.parseObject(add);
            JSONObject updateJson = JSONObject.parseObject(update);
            JSONObject delateJson = JSONObject.parseObject(delete);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String currentTime = simpleDateFormat.format(new Date());
            if(!MysqlUtil.isInit.get()){
                MysqlUtil.init();
            }

            if(addJson.size()!=0){
                StringBuilder sql = new StringBuilder();//
                sql.append("insert into `netty_config``property`,`value`,`create_time`,`modify_time`,`is_work`,`describe`) values (");
                for (String key : addJson.keySet()){
                    sql.append("\""+key+"\","+"\""+addJson.getString(key)+"\","+"\""+key+"\",\""+currentTime+"\",\""+currentTime+"\",\"true\",\"\"");
                }
                try {
                    MysqlUtil.exec(sql.toString());
                }catch (Exception e){
                    e.printStackTrace();
                    errorCode = 1;
                }
            }

            if(delateJson.size()!=0){
                String sql = "";
                for (String key : delateJson.keySet()){
                    sql = "update `netty_config` set `is_work` = \"false\" "+
                            " where `property`= \""+key+"\"";
                    try {
                        MysqlUtil.exec(sql);
                    }catch (Exception e){
                        e.printStackTrace();
                        errorCode = 1;
                    }
                    try {
                        MysqlUtil.exec(sql);
                    }catch (Exception e){
                        e.printStackTrace();
                        errorCode = 1;
                    }

                }
            }

            if(updateJson.size()!=0){
                String sql = "";
                for (String key : updateJson.keySet()){
                    sql = "update `netty_config` set `value` = \""+updateJson.getString(key)+ " `modify_time`=\""+currentTime+"\""+
                            " where `property`= \""+key+"\"";
                    try {
                        MysqlUtil.exec(sql);
                    }catch (Exception e){
                        e.printStackTrace();
                        errorCode = 1;
                    }

                }
            }

            BaseResultBean baseResultBean = new BaseResultBean();

            try {


                baseResultBean.setErrorCode(errorCode);
                baseResultBean.setMessage(errorMsg);
                resp.getWriter().write(JSONObject.toJSONString(baseResultBean));

            }catch (Exception e){
                baseResultBean.setErrorCode(1);
                baseResultBean.setMessage("更新错误");
                resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
            }
        }

    }
}
