package com.shm.metro.registry;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * 服务注册
 *
 * @author huangyong
 * @author luxiaoxun
 */
public class ServiceRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private ZooKeeper zk = null;

    private String registerData = "";

    //private volatile AtomicInteger numOfZookeeperTry = new AtomicInteger(0);

    //private volatile boolean isconnected = false;

    private Properties properties;

    public ServiceRegistry(Properties properties,String registerData){
        this.properties = properties;
        this.registerData = registerData;
        zk = connectServer(false);
    }

    public ZooKeeper getZk(){
        return zk;
    }

    public void register() {
        //zk = connectServer();
        if (zk != null) {
            AddRootNode(zk); // Add root node if not exist
            createNode(zk, registerData);
        }
    }

    private synchronized ZooKeeper connectServer(final boolean isRegisterData) {
        //ZooKeeper zk = null;
        String registryAddress = properties.getProperty(Constant.ZK_QUORUM);
        //System.out.println(Integer.valueOf(properties.getProperty(Constant.ZK_SESSION_TIMEOUT, Constant.ZK_SESSION_TIMEOUT_DEFAULT)));
        try {
            if(zk!=null){
                try{
                    zk.close();
                }catch (Exception e){}
            }
            //numOfZookeeperTry.incrementAndGet();
            zk = new ZooKeeper(registryAddress, Integer.valueOf(properties.getProperty(Constant.ZK_SESSION_TIMEOUT, Constant.ZK_SESSION_TIMEOUT_DEFAULT)), new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                        LOGGER.warn("connected to zookepper:" + event.toString());
                        if(isRegisterData) {
                            register();
                        }
                    }
                    reconnection(event);
                }

            });

            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("fail to connected to zookepper:" + registryAddress+ ",message=>"+e.getMessage());
        }


        return zk;
    }

    private void AddRootNode(ZooKeeper zk){
        try {
            Stat s = zk.exists(properties.getProperty(Constant.ZK_REGISTRY_PATH, Constant.ZK_REGISTRY_PATH_DEFAULT), false);
            if (s == null) {
                zk.create(properties.getProperty(Constant.ZK_REGISTRY_PATH, Constant.ZK_REGISTRY_PATH_DEFAULT),
                        new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (KeeperException e) {
            LOGGER.error(e.toString());
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }
    }

    private void createNode(ZooKeeper zk, String data) {
        try {
            byte[] bytes = data.getBytes();
            String path = zk.create(properties.getProperty(Constant.ZK_REGISTRY_PATH, Constant.ZK_REGISTRY_PATH_DEFAULT)+properties.getProperty(Constant.ZK_DATA_PATH, Constant.ZK_DATA_PATH_DEFAULT),
                    bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug("create zookeeper node ({} => {})", path, data);
            }
            addWatcher(path);
        } catch (KeeperException e) {
            LOGGER.error("", e);
        }
        catch (InterruptedException ex){
            LOGGER.error("", ex);
        }
    }
    private void addWatcher(String path){
        try {

            zk.exists(properties.getProperty(Constant.ZK_DATA_PATH, Constant.ZK_DATA_PATH_DEFAULT)+path, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    reconnection(event);
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void reconnection(WatchedEvent event) {
        if (event.getState() == Watcher.Event.KeeperState.Expired) {
            LOGGER.warn("zookepper Expired:" + event.toString());
            try{
                Thread.sleep(10000);
            }catch (Exception e){}
            connectServer(true);

            //register(registerData);
        }
        if (event.getState() == Watcher.Event.KeeperState.Disconnected) {
            LOGGER.warn("zookepper Disconnected:" + event.toString());
            try{
                Thread.sleep(10000);
            }catch (Exception e){}
            connectServer(true);

            //register(registerData);
        }
    }
}
