package com.shm.metro.jettyserver.handler;

import com.alibaba.fastjson.JSONObject;
import com.shm.metro.jettyserver.Common;
import com.shm.metro.jettyserver.bean.BaseResultBean;
import com.shm.metro.jettyserver.bean.DispactchInfoBean;
import com.shm.metro.jettyserver.util.PropertyUtil;
import com.shm.metro.util.SSHUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2023/10/4.
 */
public class DispatchJar extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        //分发配置文件
        final String localDir = Common.localJarFilePath;
        final String remoteDir = Common.remoteJarFilePath;

        DispactchInfoBean baseResultBean = new DispactchInfoBean();

        String[] servers = Common.allServer.split(",");
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


        Map<String,Boolean> uploadResult = new HashMap<>();
        if(Common.allServer.equals("")){
            baseResultBean.setErrorCode(1);
            baseResultBean.setMessage("nettyServer机器不能为空！");
            baseResultBean.setCmd("endall");
            baseResultBean.setResult(uploadResult);
            resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
            resp.getWriter().flush();
            return;
        }

        Common.executorService.execute(new Runnable() {

            @Override
            public void run() {
                for (String host: Common.allServer.split(",")) {
                    SSHUtil sshUtil = new SSHUtil();
                    boolean result = sshUtil.uploadLocalFileToRemote(host.split(":")[0],
                            Common.port,
                            Common.username,
                            Common.password,
                            localDir,
                            remoteDir,
                            getSingleConfigFileSize(localDir),
                            Common.jarFileStatus);
                    Common.jarFileResult.put(host.split(":")[0],result);
                }
            }
        });




        baseResultBean.setErrorCode(0);
        baseResultBean.setMessage("success");
        baseResultBean.setCmd("start");
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
}
