package com.shm.metro.decoder;

import com.alibaba.fastjson.JSONObject;
import com.shm.metro.bean.KnHeadInfo;
import com.shm.metro.util.CrcUtil;
import com.shm.metro.util.Util;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.shm.metro.util.Util.bytesToHexString;


public class KnTCPStreamDecoder extends ByteToMessageDecoder {

    private List<Byte> readByte = new ArrayList<>();

    private static final Logger logger = Logger.getLogger("kangNiLogger");

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Date eventDate = null;

    private boolean recordLog = Boolean.parseBoolean(Util.getConfig().getProperty("recored_log"));

    private static KnHeadInfo knHeadInfo = new KnHeadInfo();

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        logger.info("添加了"+this);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        logger.info("移除了"+this);
        readByte.clear();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if(logger.isInfoEnabled()) {
            logger.info("读取信息！");
        }
        Object decode = decode(ctx,in);
        if(decode != null){
            out.add(decode);
        }
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
    {
        int msgHeaderLength = 20;
        byte[] msgHeader = new byte[msgHeaderLength];

        if(in.readableBytes() < msgHeaderLength) {
            if(logger.isInfoEnabled()) {
                logger.info("KangNi 报头长度不够，重新读取！");
            }
            return null;
        }
        if (readByte.size() != 0) {
            for (int i = 0; i < readByte.size(); i++) {
                msgHeader[i] = readByte.get(i);
            }
            readByte.clear();
        }else {
            in.readBytes(msgHeader);
            if(logger.isInfoEnabled()) {
                logger.info("读取报头信息！");
            }
        }

        ByteBuffer msgHeaderByteBuffer = ByteBuffer.wrap(msgHeader);
        msgHeaderByteBuffer.order(ByteOrder.BIG_ENDIAN);
        knHeadInfo.setByteBuffer(msgHeaderByteBuffer, 0);

        //消息头特征码
//        byte[] headCodeByte = new byte[2];
//        System.arraycopy(msgHeader, 0, headCodeByte, 0, 2);

//        byte[] dataLengthByte = new byte[2];
//        System.arraycopy(msgHeader, 0, dataLengthByte, 0, 2);
//        int dataLength = Integer.parseInt(bytesToHex(dataLengthByte), 16);

        String headCode = knHeadInfo.headCodeByte.toString();
        if(!"11265".equals(headCode)){
            logger.info("kangNi headCode error ");
            return null;
        }

        //报文长度
        long dataLength = knHeadInfo.dataLength.get();
        long datalen = dataLength - msgHeaderLength;
        if (in.readableBytes() < datalen ) {
            for (int i = 0; i < msgHeader.length; i++) {
                readByte.add(msgHeader[i]);
            }
            return null;
        }

        //报文类型
        int type = knHeadInfo.type.get();
        //报文id
        long msgId = knHeadInfo.msgId.get();
        //列车编号
        int trainNo = knHeadInfo.trainNo.get();
        //车厢号
        int coachNo = knHeadInfo.coachNo.get();
        //车厢号
        int doorNo = knHeadInfo.doorNo.get();
        int version = knHeadInfo.version.get();
        try {
            eventDate = simpleDateFormat.parse((knHeadInfo.year.get()+ 2000) + "-"+
                    knHeadInfo.month.get()+"-"+
                    knHeadInfo.day.get()+" "+
                    knHeadInfo.hour.get() + ":" +
                    knHeadInfo.minute.get() + ":" +
                    knHeadInfo.second.get());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //crc以及报尾
        int tailLen = 2;
        //payload部分
        long bodyLen = datalen - tailLen;
        byte[] body = new byte[(int)bodyLen];
        in.readBytes(body);
        byte[] msgTail = new byte[tailLen];
        in.readBytes(msgTail);
        //完整报文
        byte[] singlePackage = Util.concatAll(msgHeader,body,msgTail);
        //响应消息报文
        byte[] responseByte = CrcUtil.setParamCRC(msgHeader);
        try {
            saveToLog(msgId, trainNo, eventDate.getTime(), singlePackage);
        }catch (Exception e){
            logger.warn("trainNo:"+ trainNo + " info is null");
        }

        if(logger.isInfoEnabled()) {
            logger.info(trainNo);
        }

        JSONObject decodeJson = new JSONObject();
        decodeJson.put("trainNo", trainNo);
        decodeJson.put("msgId", msgId);
        decodeJson.put("packageByte", singlePackage);
        decodeJson.put("responseByte", responseByte);
        return decodeJson;
    }

    /**
     * 记录报文日志
     * @param msgId
     * @param trainNo
     * @param time
     * @param buffer
     */
    private void saveToLog(long msgId, int trainNo, long time, byte[] buffer) {
        if(recordLog) {
            logger.warn("receive data,train_no:" + trainNo +",msg_id:" + msgId + ",time:" + time + ",value:" + bytesToHexString(buffer));
        }
    }

}
