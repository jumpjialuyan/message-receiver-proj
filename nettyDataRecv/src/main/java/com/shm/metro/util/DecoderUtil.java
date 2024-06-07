package com.shm.metro.util;

/**
 * Created by Administrator on 2023/8/7.
 */
public class DecoderUtil {

    public static int getHeaderLength(){
        return 22;
    }

    public static int getMesgHeaderLength(){
        return 3;
    }

    public static boolean isHeader(byte[] bytes){
        return Util.byte2hex(bytes).substring(0,6).toLowerCase().equals("aaabac");
    }
}
