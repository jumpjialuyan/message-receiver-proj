package com.shm.metro.jettyserver;

import com.shm.metro.jettyserver.bean.ConfigFileInfo;
import com.shm.metro.jettyserver.monitor.MonitorThread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2023/10/2.
 */
public class Common {
    public static String remoteJarFilePath = "/home/mlamp";
    public static String remoteConfigFilePath = "/etc/";
    public static String startCmd = "nohup java -cp "+remoteJarFilePath+"/shmetro-1.0-SNAPSHOT-jar-with-dependencies.jar com.shm.metro.core.TCPServer 2>&1 >/dev/null &";
    public static String stopCmd = "ps -ef | grep '"+remoteJarFilePath+"/shmetro-1.0-SNAPSHOT-jar-with-dependencies.jar com.shm.metro.core.TCPServer' | grep -v grep | awk '{print $2}' | xargs kill -9";
    public static String className="com.shm.metro.core.TCPServer";
    public static String pidFile = "/tmp/nettydatarecv.pid";
    public static String localJarFilePath = "/Users/lixiaohuan/IdeaProjects/shmetro/target/shmetro-1.0-SNAPSHOT.jar";
    public static String pidCmd = "echo $!>"+pidFile;
    public static MonitorThread monitorThread= null;
    public static String allServer = "";
    public static String username = "";
    public static String password = "mlamp123456";
    public static int port = 22;
    public static Map<String,Boolean> configFileResult = new ConcurrentHashMap<>();
    public static Map<String,ConfigFileInfo> configFileStatus = new ConcurrentHashMap<>();
    public static Map<String,ConfigFileInfo> jarFileStatus = new ConcurrentHashMap<>();
    public static Map<String,Boolean> jarFileResult = new ConcurrentHashMap<>();
    public static ExecutorService executorService = Executors.newCachedThreadPool();
}
