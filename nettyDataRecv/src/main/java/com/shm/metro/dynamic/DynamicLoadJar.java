package com.shm.metro.dynamic;

import com.shm.metro.bean.JCLBean;
import com.shm.metro.function.Function;
import com.shm.metro.interfacer.IProtocol;
import com.shm.metro.util.HDFSUtil;
import org.apache.log4j.Logger;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.JclUtils;

import java.io.InputStream;
import java.io.Serializable;


public class DynamicLoadJar implements Serializable {

    private static JarClassLoader jcl = null;

    private static JclObjectFactory factory = null;

    private static Logger logger = Logger.getLogger(DynamicLoadJar.class);

    public synchronized static IProtocol reload(final String jarPath, String className) throws Exception {

        if (jcl == null) {
            jcl = new JarClassLoader();
        } else {
            jcl = null;
            jcl = new JarClassLoader();
        }


        jcl.getSystemLoader().setOrder(1); // Look in system class loader first
        jcl.getLocalLoader().setOrder(2); // if not found look in local class loader
        jcl.getParentLoader().setOrder(3); // if not found look in parent class loader
        jcl.getThreadLoader().setOrder(4); // if not found look in thread context class loader
        jcl.getCurrentLoader().setOrder(5); // if not found look in current class loader

        HDFSUtil.getInputStreamFromHDFS(jarPath, new Function<InputStream>() {
            @Override
            public void call(InputStream v1) {
                jcl.add(v1);
                logger.info("load jar input stream from hdfs success.");
            }
        });

        //jcl.add(new URL(protocol+jarPath));

        JclObjectFactory factory = JclObjectFactory.getInstance();

        //Create object of loaded class
        Object obj = factory.create(jcl, className);

        IProtocol iProtocol = (IProtocol) JclUtils.toCastable(obj, IProtocol.class);

        logger.info("new JarClassLoader success....");

        logger.info("load class " + className + " success.");

        return iProtocol;
    }

    public synchronized static JCLBean reloadJCL(final String jarPath, String className) throws Exception {

        if (jcl == null) {
            jcl = new JarClassLoader();
        } else {
            jcl = null;
            jcl = new JarClassLoader();
        }


        jcl.getSystemLoader().setOrder(1); // Look in system class loader first
        jcl.getLocalLoader().setOrder(2); // if not found look in local class loader
        jcl.getParentLoader().setOrder(3); // if not found look in parent class loader
        jcl.getThreadLoader().setOrder(4); // if not found look in thread context class loader
        jcl.getCurrentLoader().setOrder(5); // if not found look in current class loader

        HDFSUtil.getInputStreamFromHDFS(jarPath, new Function<InputStream>() {
            @Override
            public void call(InputStream v1) {
                jcl.add(v1);
                logger.info("load jar input stream from hdfs success.");
            }
        });

        //jcl.add(new URL(protocol+jarPath));

        JclObjectFactory factory = JclObjectFactory.getInstance();

        logger.info("new JarClassLoader success....");

        logger.info("load class " + className + " success.");

        return new JCLBean(factory,jcl,className);
    }

    public synchronized static IProtocol reloadFromLocal(final String jarPath, String className) throws Exception {

        if (jcl == null) {
            jcl = new JarClassLoader();
        } else {
            jcl = null;
            jcl = new JarClassLoader();
        }

        jcl.initialize();


        logger.info("new JarClassLoader success....");
        jcl.getSystemLoader().setOrder(1); // Look in system class loader first
        jcl.getLocalLoader().setOrder(2); // if not found look in local class loader
        jcl.getParentLoader().setOrder(3); // if not found look in parent class loader
        jcl.getThreadLoader().setOrder(4); // if not found look in thread context class loader
        jcl.getCurrentLoader().setOrder(5); // if not found look in current class loader

        HDFSUtil.getInputStreamFromLocal(jarPath, new Function<InputStream>() {
            @Override
            public void call(InputStream v1) {
                jcl.add(v1);
                logger.info("load jar input stream from local success.");
            }
        });

        /*for (URL url:((URLClassLoader)Thread.currentThread().getContextClassLoader()).getURLs()){
            //System.out.println(url);
            if(url.getPath().contains("jre")){
                continue;
            }
            jcl.add(url);
        }*/
        //jcl.add(new URL(protocol+jarPath));

        //ProxyProviderFactory.setDefaultProxyProvider( new CglibProxyProvider() );

        JclObjectFactory factory = JclObjectFactory.getInstance();

        //Create object of loaded class
        Object obj = factory.create(jcl, className);

        IProtocol iProtocol = (IProtocol) JclUtils.toCastable(obj, IProtocol.class);

        logger.info("load class " + className + " success.");

        return iProtocol;
    }


}

