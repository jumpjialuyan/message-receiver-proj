package com.shm.metro.util;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.*;
import com.shm.metro.jettyserver.bean.ConfigFileInfo;
import com.shm.metro.jettyserver.bean.DispactchInfoBean;
import com.sun.jersey.api.core.HttpRequestContext;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.*;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2023/10/3.
 */
public class SSHUtil {

    private static org.apache.log4j.Logger logger = Logger.getLogger(SSHUtil.class);

    public static void main(String[] args) throws IOException, JSchException {
        // TODO Auto-generated method stub
        String host = "sh2.mlamp.co";
        int port = 22;
        String username = "root";
        String password = "mlamp123456";
        String command = "ps -ef";
        Session session=null;
        //String res = exeCommand(host,port,username,password,command);
        //System.out.println(res);

    }

    public  void test() throws IOException, JSchException {
        // TODO Auto-generated method stub
        String host = "sh2.mlamp.co";
        int port = 22;
        String username = "root";
        String password = "mlamp123456";
        String command = "ps -ef";
        Session session=null;
        String res = exeCommand(host,port,username,password,command);
        System.out.println(res);

    }


    public  String exeCommand(String host, int port, String user, String password, String command) throws JSchException, IOException {

        Session session = openSession(host,port,user,password);
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        InputStream in = channelExec.getInputStream();
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        String out = IOUtils.toString(in, "UTF-8");

        channelExec.disconnect();
        session.disconnect();
        System.out.println(out);
        return out;
    }
    private  Session openSession(String host, int port, String user, String password) throws JSchException{
        JSch jsch=new JSch();
        Session session=jsch.getSession(user,host,port);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.setPassword("mlamp123456");
        session.connect(30000);
        return session;
    }

    public  boolean uploadLocalFileToRemote(String host, int port, String user, String password, String localFile, String remoteDir, Long filesize, Map<String,ConfigFileInfo> status) {

        Session session=null;
        try {
            session = openSession(host,port,user,password);
        } catch (JSchException e) {
            logger.error(e.getMessage());
            if(session!=null) session.disconnect();
            return false;
        }
        ChannelSftp channel=null;
        try {
            ConfigFileInfo configFileInfo = new ConfigFileInfo();
            status.put(host,configFileInfo);
            configFileInfo.setServer(host);
            configFileInfo.setTotalSize(filesize);
            configFileInfo.setUploadedSize(0L);
            channel=(ChannelSftp) session.openChannel("sftp");
            channel.connect();
            SftpProgressMonitorImpl sftpProgressMonitorImpl=new SftpProgressMonitorImpl(configFileInfo);
            channel.put(localFile, remoteDir,sftpProgressMonitorImpl);

            return sftpProgressMonitorImpl.isSuccess();
        }catch (JSchException e) {
            if(channel!=null){
                channel.disconnect();
                session.disconnect();
            }
            return  false;
        } catch (SftpException e) {
            logger.error(e.getMessage());
        }
        return false;
    }
    static class  SftpProgressMonitorImpl implements SftpProgressMonitor{
        private  long size;
        private  long currentSize=0;
        private  boolean endFlag=false;
        private  ConfigFileInfo configFileInfo;


        public SftpProgressMonitorImpl(ConfigFileInfo configFileInfo){
            this.configFileInfo  = configFileInfo;
        }

        @Override
        public void init(int op, String srcFile, String dstDir, long size) {
            logger.info("文件开始上传：【"+srcFile+"】-->;【"+dstDir+"】"+"，文件大小:"+size+",参数"+op);
            this.size=size;
        }

        @Override
        public void end() {
            logger.info("文件上传结束");
            /*DispactchInfoBean baseResultBean = new DispactchInfoBean();
            baseResultBean.setErrorCode(0);
            baseResultBean.setMessage("success");
            baseResultBean.setCmd("ending");
            try {
                response.getWriter().write(JSONObject.toJSONString(baseResultBean));
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            this.configFileInfo.setUploaded(true);
            endFlag=true;
        }

        @Override
        public boolean count(long count){
            currentSize+=count;
            logger.info("上传数量"+currentSize);
            this.configFileInfo.setUploadedSize(currentSize);
            /*DispactchInfoBean baseResultBean = new DispactchInfoBean();
            baseResultBean.setErrorCode(0);
            baseResultBean.setMessage("success");
            baseResultBean.setCmd("uploading");
            baseResultBean.setCurrentUploadSize(count);
            try {
                response.getWriter().write(JSONObject.toJSONString(baseResultBean));
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            return true;
        }
        public boolean isSuccess(){
            return endFlag&&currentSize==size;
        }
    }
}

