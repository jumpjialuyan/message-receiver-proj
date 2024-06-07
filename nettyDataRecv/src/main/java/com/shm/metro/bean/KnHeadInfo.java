package com.shm.metro.bean;

import javolution.io.Struct;

import java.nio.ByteOrder;

/**
 * Created by TaoYingAn on 2018/12/20.
 */
public class KnHeadInfo extends Struct {
    //报头特征码
    public final BitField headCodeByte = new BitField(16);
    //报文长度
    public final Unsigned16 dataLength = new Unsigned16();
    //报文类型
    public final Unsigned8 type = new Unsigned8();
    //帧号（报文id）
    public final Unsigned16 msgId = new Unsigned16();
    //列车编号
    public final Unsigned16 trainNo = new Unsigned16();
    //车厢号
    public final Unsigned8 coachNo = new Unsigned8();
    //车门编号
    public final Unsigned8 doorNo = new Unsigned8();
    //协议版本
    public final Unsigned8 version = new Unsigned8();
    //时间-年
    public final Unsigned8 year = new Unsigned8();
    //时间-月
    public final Unsigned8 month = new Unsigned8();
    //时间-日
    public final Unsigned8 day = new Unsigned8();
    //时间-时
    public final Unsigned8 hour = new Unsigned8();
    //时间-分
    public final Unsigned8 minute = new Unsigned8();
    //时间-秒
    public final Unsigned8 second = new Unsigned8();
    //预留
    public final BitField reserve = new BitField(16);

    public ByteOrder byteOrder() {
        return ByteOrder.BIG_ENDIAN;
    }
    public boolean isPacked() {return true;}
}
