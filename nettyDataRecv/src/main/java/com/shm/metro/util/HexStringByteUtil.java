package com.shm.metro.util;

//import org.apache.hadoop.hbase.util.Bytes;

import javax.xml.bind.DatatypeConverter;

/**
 * Created by wgh on 2023/8/1.
 */
public class HexStringByteUtil {
    static char[] HEXA = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    /**
     * 将一个byte 数值 以 可见的hexString方式 输出
     * @param a
     * @return
     */
    public static String intToString(int a){
        a = a & 0xff;
        char[] c = new char[2];

        c[0] = HEXA[a>>4];
        c[1] = HEXA[a&15];

        return new String(c);
    }

    public static String showByteArray(byte[] val) {
        String s = "";
        for (byte b : val) {
            //s += String.format("\\x%x", b).toString();
            s += intToString(b);
        }
        System.out.println("Value: " + s);
        return s;

    }
    public static byte[] converLongToBytes(long l) {
        byte[] b = new byte[8];

        b = Long.toString(l).getBytes();

        return b;
    }

    public static  byte[] byteToArray(byte b){
        return new byte[]{b};
    }
    public static void main(String args[]){
//        for (int i =0 ;i <=255;i++){
//            String hexString = intToString(i);
//            int num = Integer.parseInt(hexString, 16);
//            System.out.println(hexString + "," + num);
//        }

        showByteArray(converLongToBytes(7l));
        showByteArray(new byte[]{0x07});
        long a = 7l;
//        byte []b = Bytes.toBytes(a);
//        showByteArray(b);
//
//        long time = System.currentTimeMillis();
//        byte []c = Bytes.toBytes(time);
//        System.out.printf("%x \n", time);
//        showByteArray(c);
//        System.out.println(intToString((byte) 0xff));
//
//        showByteArray(Bytes.toBytes("hello world!"));
//
//        String hex = "0A000C0B";
//
//        showByteArray(Bytes.toBytes(hex));
//        showByteArray(Bytes.fromHex(hex));
//
//
//        showByteArray(Bytes.toBytes(5));
//        showByteArray(Bytes.toBytes((byte)0x05));
//        showByteArray(byteToArray((byte)0x05));
//
//        showByteArray(Bytes.toBytes(true));
    }
}
