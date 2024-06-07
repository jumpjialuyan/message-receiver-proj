package com.shm.metro.bean;

import com.shm.metro.interfacer.IProtocol;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.JclUtils;

/**
 * Created by Administrator on 2023/10/30.
 */
public class JCLBean {
    private JclObjectFactory jclObjectFactory;
    private JarClassLoader jarClassLoader;
    private String className;


    public JCLBean(JclObjectFactory jclObjectFactory, JarClassLoader jarClassLoader, String className) {
        this.jclObjectFactory = jclObjectFactory;
        this.jarClassLoader = jarClassLoader;
        this.className = className;
    }

    public synchronized IProtocol getIProtocol(){
        Object o = this.jclObjectFactory.create(jarClassLoader,this.className);

        return (IProtocol) JclUtils.toCastable(o, IProtocol.class);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public JclObjectFactory getJclObjectFactory() {
        return jclObjectFactory;
    }

    public void setJclObjectFactory(JclObjectFactory jclObjectFactory) {
        this.jclObjectFactory = jclObjectFactory;
    }

    public JarClassLoader getJarClassLoader() {
        return jarClassLoader;
    }

    public void setJarClassLoader(JarClassLoader jarClassLoader) {
        this.jarClassLoader = jarClassLoader;
    }
}
