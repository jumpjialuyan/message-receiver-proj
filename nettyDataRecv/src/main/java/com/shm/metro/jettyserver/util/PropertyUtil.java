package com.shm.metro.jettyserver.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2023/10/2.
 */
public class PropertyUtil {
    public static final  String configFilePath = "/tmp/nettymetadb.conf";

    public static final String nettyConfigFilePath = "/tmp/nettydatarecv.conf";

    private static Properties properties;

    static {
        try {
            initProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void refresh() throws IOException {
        initProperties();
    }

    private static void initProperties() throws IOException {
        properties = new Properties();

        if(!Files.exists(Paths.get(configFilePath))){
            Files.createFile(Paths.get(configFilePath));
        }
        properties.load(new FileInputStream(configFilePath));

    }

    public static void newConfigrateFile(Map<String,String> properties){

        Path path = Paths.get(nettyConfigFilePath);

        if(!Files.exists(path)){
            mkFile(nettyConfigFilePath);
        }
        Properties property = new Properties();
        try {
            property.load(new FileInputStream(nettyConfigFilePath));

            for (String key:properties.keySet()){
                property.setProperty(key,properties.get(key));
            }

            try {
                OutputStream fos = new FileOutputStream(nettyConfigFilePath);
                property.store(fos, "Update value");
                fos.close();
            } catch (IOException e) {
                System.err.println("属性文件更新错误,"+e.getMessage());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //property = null;
        }
    }
    public static boolean isPropertiesFileExist(){
        return Files.exists(Paths.get(configFilePath));
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    public static String getProperty(String key,String defaultValue){
        return properties.getProperty(key,defaultValue);
    }

    public static void reWriteProperty(String key,String value){
        properties.setProperty(key,value);

    }

    public static void mkFile(String filePath){
        try {
            Files.createFile(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void flush() {
        try {

            OutputStream fos = new FileOutputStream(configFilePath);
            properties.store(fos, "Update value");
            fos.close();
        } catch (IOException e) {
            System.err.println("属性文件更新错误,"+e.getMessage());
        }
    }



}
