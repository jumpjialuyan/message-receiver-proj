package com.shm.metro.jettyserver.handler;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.JSchException;
import com.shm.metro.jettyserver.Common;
import com.shm.metro.jettyserver.bean.BaseResultBean;
import com.shm.metro.jettyserver.bean.NettyStatusBean;
import com.shm.metro.jettyserver.bean.StartInfoBean;
import com.shm.metro.jettyserver.monitor.MonitorThread;
import com.shm.metro.jettyserver.util.PropertyUtil;
import com.shm.metro.util.SSHUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2023/10/2.
 */
public class StartNetty extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        Map<String,String> result = new HashMap<>();
        StartInfoBean baseResultBean = new StartInfoBean();
        for (String host: Common.allServer.split(",")) {
            try {
                SSHUtil sshUtil = new SSHUtil();
                String out = sshUtil.exeCommand(host.split(":")[0],
                        Common.port,
                        Common.username,
                        Common.password,
                        Common.startCmd);

                result.put(host.split(":")[0],out);
            } catch (JSchException e) {
                e.printStackTrace();
                baseResultBean.setErrorCode(1);
                baseResultBean.setMessage(e.getMessage());
                resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
                return;
            }
        }

        baseResultBean.setErrorCode(0);
        baseResultBean.setMessage("success");
        baseResultBean.setResult(result);
        resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
    }
}
