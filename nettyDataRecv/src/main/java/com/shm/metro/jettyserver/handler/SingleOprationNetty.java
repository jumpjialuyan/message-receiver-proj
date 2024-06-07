package com.shm.metro.jettyserver.handler;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.JSchException;
import com.shm.metro.jettyserver.Common;
import com.shm.metro.jettyserver.bean.StartInfoBean;
import com.shm.metro.util.SSHUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2023/10/2.
 */
public class SingleOprationNetty extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cmd = req.getParameter("cmd");
        String host = req.getParameter("host");
        resp.setContentType("text/html;charset=UTF-8");
        Map<String, String> result = new HashMap<>();
        StartInfoBean baseResultBean = new StartInfoBean();
        host = host.split(":")[0];
        if(cmd.equals("start")) {
            try {
                SSHUtil sshUtil = new SSHUtil();
                String out = sshUtil.exeCommand(host,
                        Common.port,
                        Common.username,
                        Common.password,
                        Common.startCmd);

                result.put(host.split(":")[0], out);
            } catch (JSchException e) {
                e.printStackTrace();
                baseResultBean.setErrorCode(1);
                baseResultBean.setMessage(e.getMessage());
                resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
                return;
            }
        }else if(cmd.equals("stop")){
            try {
                SSHUtil sshUtil = new SSHUtil();
                String out = sshUtil.exeCommand(host,
                        Common.port,
                        Common.username,
                        Common.password,
                        Common.stopCmd);

                result.put(host.split(":")[0], out);
            } catch (JSchException e) {
                e.printStackTrace();
                baseResultBean.setErrorCode(1);
                baseResultBean.setMessage(e.getMessage());
                resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
                return;
            }
        } else if (cmd.equals("restart")) {
            try {
                SSHUtil sshUtil = new SSHUtil();
                String out = sshUtil.exeCommand(host,
                        Common.port,
                        Common.username,
                        Common.password,
                        Common.stopCmd);

                result.put(host.split(":")[0], out);
            } catch (JSchException e) {
                e.printStackTrace();
                baseResultBean.setErrorCode(1);
                baseResultBean.setMessage(e.getMessage());
                resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
                return;
            }
            try {
                SSHUtil sshUtil = new SSHUtil();
                String out = sshUtil.exeCommand(host,
                        Common.port,
                        Common.username,
                        Common.password,
                        Common.startCmd);

                result.put(host.split(":")[0], out);
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
