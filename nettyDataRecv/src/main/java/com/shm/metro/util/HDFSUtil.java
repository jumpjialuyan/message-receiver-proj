package com.shm.metro.util;

import com.alibaba.fastjson.JSONObject;
import com.shm.metro.function.Function;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.shm.metro.util.CommonVar.JAR_FILE_PATH_CONFIG;
import static com.shm.metro.util.Util.BASE_PATH;

/**
 * Created by Administrator on 2023/8/10.
 */
public class HDFSUtil {
    private static final String hdfsSiteConfigPath = BASE_PATH + "hdfs-site.xml";
    private static final String hdfsCoreConfigPath = BASE_PATH + "core-site.xml";

    public static boolean getInputStreamFromHDFS(String path, Function<InputStream> function){
        try {
            Configuration conf = new Configuration();
            if(Files.exists(Paths.get(hdfsSiteConfigPath))){
                conf.addResource(new Path(hdfsSiteConfigPath));
            }
            if(Files.exists(Paths.get(hdfsCoreConfigPath))) {
                conf.addResource(new Path(hdfsCoreConfigPath));
            }
            conf.setBoolean("fs.hdfs.impl.disable.cache", true);
            FileSystem fs = FileSystem.get(URI.create(path), conf);
            FSDataInputStream fsdi = fs.open(new Path(path));
            function.call(fsdi);
            fsdi.close();
            fs.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getInputStreamFromLocal(String path, Function<InputStream> function){
        try {

            FileInputStream fsdi = new FileInputStream(path);
            function.call(fsdi);
            fsdi.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public synchronized static String getNewestJarHDFS(){
        System.setProperty("HADOOP_USER_NAME", "hdfs");
        Configuration conf = new Configuration();
        if (Files.exists(Paths.get(hdfsSiteConfigPath))) {
            conf.addResource(new Path(hdfsSiteConfigPath));
        }
        if (Files.exists(Paths.get(hdfsCoreConfigPath))) {
            conf.addResource(new Path(hdfsCoreConfigPath));
        }
        conf.setBoolean("fs.hdfs.impl.disable.cache", true);

        // 遍历目录下的所有文件
        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);

            if(!fs.exists(new Path(JAR_FILE_PATH_CONFIG))){
                FSDataOutputStream fsDataOutputStream = fs.create(new Path(JAR_FILE_PATH_CONFIG),true);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fileName",CommonVar.DEFAILT_JAR_FILE_PATH);
                fsDataOutputStream.writeBytes(jsonObject.toJSONString());
                fsDataOutputStream.close();
                return CommonVar.DEFAILT_JAR_FILE_PATH;
            }else{
                FSDataInputStream inputStream = fs.open(new Path(JAR_FILE_PATH_CONFIG));
                byte[] buffer = new byte[1024];
                StringBuilder stringBuilder = new StringBuilder();
                int readCount = 0;
                while ((readCount = inputStream.read(buffer))!=-1){
                    stringBuilder.append(new String(buffer,0,readCount));
                }
                inputStream.close();
                return (String)JSONObject.parseObject(stringBuilder.toString()).get("fileName");
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fs != null){
                try {
                    fs.close();
                }catch (Exception e){}
            }
        }
        return CommonVar.DEFAILT_JAR_FILE_PATH;

    }

    public synchronized static String setNewestJarToHDFS(String filePath) throws IOException {
        System.setProperty("HADOOP_USER_NAME", "hdfs");
        Configuration conf = new Configuration();
        if (Files.exists(Paths.get(hdfsSiteConfigPath))) {
            conf.addResource(new Path(hdfsSiteConfigPath));
        }
        if (Files.exists(Paths.get(hdfsCoreConfigPath))) {
            conf.addResource(new Path(hdfsCoreConfigPath));
        }
        conf.setBoolean("fs.hdfs.impl.disable.cache", true);
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        // 遍历目录下的所有文件
        FileSystem fs = null;
        try {
            fs = FileSystem.get(conf);


            FSDataOutputStream fsDataOutputStream = fs.create(new Path(JAR_FILE_PATH_CONFIG),true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fileName",filePath);
            fsDataOutputStream.writeBytes(jsonObject.toJSONString());
            fsDataOutputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fs != null){
                fs.close();
            }
        }
        return null;

    }
}
