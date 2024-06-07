package com.shm.metro.jettyserver.handler;

import com.alibaba.fastjson.JSONObject;
import com.shm.metro.jettyserver.Common;
import com.shm.metro.jettyserver.bean.NettyStatusBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2023/10/3.
 */
public class ServerStatusHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        NettyStatusBean nettyStatusBean = new NettyStatusBean();

        nettyStatusBean.setErrorCode(0);

        nettyStatusBean.setMessage("");

        String[] servers = Common.allServer.split(",");

        nettyStatusBean.setAllServer(Arrays.asList(servers));

        nettyStatusBean.setOnlineServer(Common.monitorThread.getRunningServer());

        for (String server : nettyStatusBean.getAllServer()){
            nettyStatusBean.getStatus().put(server,nettyStatusBean.getOnlineServer().contains(server));
        }

        resp.getWriter().write(JSONObject.toJSONString(nettyStatusBean));
    }
}
