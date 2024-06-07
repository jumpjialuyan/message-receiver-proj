package com.shm.metro.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Administrator on 2023/7/30.
 */
public class Util {

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String getDateString(Long time){
        return formatter.format(new Date(time));
    }

    public static final String BASE_PATH="/etc/nettydatarecv/";

    public static final String CONFIG_PATH=BASE_PATH+"/baseInfo.properties";


    static Properties properties = null;

    public static String getDateStringWithFormat(Long time,String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(new Date(time));
    }

    public static void main(String[] args) {
        System.out.println("Unsigned16,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,Unsigned8,Unsigned16,Unsigned16,Unsigned8,Unsigned8,Unsigned8,INTEGER16,Unsigned8,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,ENUM4,ENUM4,INTEGER8,INTEGER8,INTEGER8,INTEGER8,BOOLEAN1,BOOLEAN1,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ENUM4,ENUM4,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ANTIVALENT2,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,ENUM4,Unsigned32,Unsigned32,Unsigned32,Unsigned32,Unsigned32,Unsigned32,Unsigned32,Unsigned32,Unsigned32,Unsigned16,Unsigned16,Unsigned16,INTEGER16,INTEGER16,INTEGER16,INTEGER16,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,BOOLEAN1,Unsigned16,INTEGER16,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,INTEGER8,Unsigned16,Unsigned8,INTEGER8,Unsigned16,Unsigned8,INTEGER8,Unsigned16,Unsigned8,INTEGER8,Unsigned16,Unsigned8,Unsigned8,Unsigned8,INTEGER8,Unsigned8,Unsigned8,Unsigned8,INTEGER8,Unsigned8,Unsigned8,Unsigned8,INTEGER8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8,Unsigned8".toUpperCase());
    }
    /**
     * 多个数组合并
     *
     * @param first
     * @param rest
     * @return
     */
    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static byte[] hex2Byte(String str)
    {
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte) Integer
                    .parseInt(str.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static String hex2bin(String hex) {
        String digital = "0123456789ABCDEF";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }

    //byte数组转成long
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }

    /**
     * 注释：字节数组到int的转换！
     *
     * @param b
     * @return
     */
    public static int byteToInt(byte[] b) {
        int s = 0;
        int s0 = b[0] & 0xff;// 最低位
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }
    /**
     * 注释：字节数组到short的转换！
     *
     * @param b
     * @return
     */
    public static short byteToShort(byte[] b) {
        short s = 0;
        short s0 = (short) (b[0] & 0xff);// 最低位
        short s1 = (short) (b[1] & 0xff);
        s1 <<= 8;
        s = (short) (s0 | s1);
        return s;
    }

    private static final char[] hexArray = "0123456789abcdef".toCharArray();
    public synchronized static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[(j * 2)] = hexArray[(v >>> 4)];
            hexChars[(j * 2 + 1)] = hexArray[(v & 0xF)];
        }
        return new String(hexChars);
    }


    public static String byte2hex(byte[] b)
    {

        // String Buffer can be used instead

        String hs = "";
        String stmp = "";

        for (int n = 0; n < b.length; n++)
        {
            stmp = (Integer.toHexString(b[n] & 0XFF));

            if (stmp.length() == 1)
            {
                hs = hs + "0" + stmp;
            }
            else
            {
                hs = hs + stmp;
            }

            if (n < b.length - 1)
            {
                hs = hs + "";
            }
        }

        return hs;
    }
    public static Properties getConfig(String zookeeper_configfilepath){
        Properties properties = null;
        try {
            ClassLoader cL = Thread.currentThread().getContextClassLoader();
            if (cL == null) {
                cL = Util.class.getClassLoader();
            }

            properties = new Properties();
            InputStream inputStream = cL.getResourceAsStream(zookeeper_configfilepath);
            properties.load(inputStream);
        }catch (Exception e){

        }
        //System.out.println(properties.getProperty("zk.quorum"));
        return properties;
    }
    public synchronized static Properties getConfig(){

        if(properties==null){
            properties = new Properties();
            Path path = Paths.get(CONFIG_PATH);
            if(Files.exists(path)){
                try {
                    properties.load(new FileInputStream(CONFIG_PATH));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return properties;
            }else {

                try {
                    ClassLoader cL = Thread.currentThread().getContextClassLoader();
                    if (cL == null) {
                        cL = Util.class.getClassLoader();
                    }

                    properties = new Properties();
                    InputStream inputStream = cL.getResourceAsStream("baseInfo.properties");
                    properties.load(inputStream);
                } catch (Exception e) {

                }
                //System.out.println(properties.getProperty("zk.quorum"));
                return properties;
            }
        }else{
            return properties;
        }

    }
}
