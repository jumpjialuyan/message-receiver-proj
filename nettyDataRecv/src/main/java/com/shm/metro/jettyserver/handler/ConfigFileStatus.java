package com.shm.metro.jettyserver.handler;

import com.alibaba.fastjson.JSONObject;
import com.shm.metro.jettyserver.Common;
import com.shm.metro.jettyserver.bean.DispactchInfoBean;
import com.shm.metro.jettyserver.bean.UploadProcessBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Administrator on 2023/11/9.
 */
public class ConfigFileStatus  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DispactchInfoBean baseResultBean = new DispactchInfoBean();
        int serverNum = Common.allServer.split(",").length;
        if(Common.configFileResult.size() == serverNum){
            for (String key : Common.configFileResult.keySet()){
                if(!Common.configFileResult.get(key)){
                    baseResultBean.setErrorCode(1);
                    baseResultBean.setMessage("分发文件失败！");
                    resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
                    resp.getWriter().flush();
                    return;
                }
            }
            baseResultBean.setMessage("success！");
            baseResultBean.setErrorCode(0);
            baseResultBean.setCmd("endall");
            baseResultBean.setResult(Common.configFileResult);
            resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
            resp.getWriter().flush();
        }else{
            UploadProcessBean uploadProcessBean = new UploadProcessBean();
            if(Common.configFileStatus.size()==0){
                uploadProcessBean.setErrorMsg("");
                uploadProcessBean.setHasError(false);
                uploadProcessBean.setTotalSize(0);
                uploadProcessBean.setUploadedSize(0);
                uploadProcessBean.setUploadPercent(0);
                baseResultBean.setUploadProcessBean(uploadProcessBean);
                baseResultBean.setCmd("endall");
                resp.getWriter().write(JSONObject.toJSONString(baseResultBean));
                resp.getWriter().flush();

            }else{

            }
        }


    }
}
