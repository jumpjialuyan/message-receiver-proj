package com.shm.metro.jettyserver.monitor;

import com.shm.metro.util.Util;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * Created by Administrator on 2023/10/2.
 */
public class MonitorThread {

    private ArrayList<String> runningServer = new ArrayList<>();

    private MonitorDaemon monitorDaemon = null;//new MonitorDaemon();

    private Thread thread;

    private Logger logger = Logger.getLogger(MonitorThread.class);

    public MonitorThread(){
        monitorDaemon = new MonitorDaemon(runningServer);
    }

    public void start(){
        thread = new Thread(monitorDaemon);
        thread.start();
        logger.info("start monitor thread");
    }
    public ArrayList<String> getRunningServer(){
        return runningServer;
    }
}

class MonitorDaemon implements Runnable{

    private Logger logger = Logger.getLogger(MonitorDaemon.class);
    private ArrayList<String> runningServer;
    public MonitorDaemon(ArrayList<String> runningServer){
        this.runningServer = runningServer;
    }
    private String rpc_configfilepath = "rpc.properties";
    @Override
    public void run() {

        try {
            //ServiceDiscovery serviceDiscovery = new ServiceDiscovery(Util.getConfig());

            while (true) {
                this.runningServer.clear();
                //this.runningServer.addAll(serviceDiscovery.getDataList());
                for (String data : this.runningServer){
                    logger.info("server :" + data + " is online");
                }
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
