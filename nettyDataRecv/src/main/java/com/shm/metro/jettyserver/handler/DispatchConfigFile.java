package com.shm.metro.jettyserver.handler;

import com.alibaba.fastjson.JSONObject;
import com.shm.metro.function.Function;
import com.shm.metro.jettyserver.Common;
import com.shm.metro.jettyserver.bean.BaseResultBean;
import com.shm.metro.jettyserver.bean.DispactchInfoBean;
import com.shm.metro.jettyserver.util.MysqlUtil;
import com.shm.metro.jettyserver.util.PropertyUtil;
import com.shm.metro.util.SSHUtil;
import org.apache.hadoop.util.hash.Hash;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2023/10/4.
 */
public class DispatchConfigFile extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        DispactchInfoBean baseResultBean = new DispactchInfoBean();
        //分发配置文件
        final String localDir = generateConfigFile();
        final String remoteDir = Common.remoteConfigFilePath;

        Map<String,Boolean> uploadResult = new HashMap<>();

        if(localDir == null){
            baseResultBean.setErrorCode(1);
            baseResultBean.setMessage("生成配置文件错误！");
            resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
            resp.getWriter().flush();
            return;
        }

        final String[] servers = Common.allServer.split(",");
        int length = servers.length;
        long totalFileSize = getSingleConfigFileSize(localDir) * length;
        if(totalFileSize ==0){
            baseResultBean.setErrorCode(1);
            baseResultBean.setMessage("获取文件大小错误！");
            resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
            resp.getWriter().flush();
            return;
        }else{
            baseResultBean.setErrorCode(0);
            baseResultBean.setMessage("success");
            //baseResultBean.setCmd("startupload");
            //baseResultBean.setTotalFile(servers.length);
            //baseResultBean.setFileTotlaSize(totalFileSize);
            /*resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
            resp.getWriter().flush();*/
        }

        Common.executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (String host: servers) {
                    SSHUtil sshUtil = new SSHUtil();
                    boolean result = sshUtil.uploadLocalFileToRemote(host.split(":")[0],
                            Common.port,
                            Common.username,
                            Common.password,
                            localDir,
                            remoteDir,
                            getSingleConfigFileSize(localDir),
                            Common.configFileStatus);
                    Common.configFileResult.put(host.split(":")[0],result);
                }
            }
        });



        baseResultBean.setErrorCode(0);
        baseResultBean.setCmd("start");
        baseResultBean.setMessage("success");
        //baseResultBean.setResult(uploadResult);

        resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
        resp.getWriter().flush();
    }



    private long getSingleConfigFileSize(String filepath){
        File file = new File(filepath);
        if(file.isFile() && file.exists()){
            return file.length();
        }else{
            return 0;
        }
    }

    private String generateConfigFile(){
        String sql = "SELECT * FROM netty_config where is_work='true'";
        final Map<String,String> map = new HashMap<>();
        MysqlUtil.exec(sql, new Function<ResultSet>() {
            @Override
            public void call(ResultSet v) {
                try {
                    String property = v.getString("property");
                    String value = v.getString("value");
                    map.put(property,value);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });

        if(map.size()!=0){
            PropertyUtil.newConfigrateFile(map);
            return PropertyUtil.nettyConfigFilePath;
        }
        return null;
    }
}
