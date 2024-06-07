/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.shm.metro.decoder;

import com.shm.metro.bean.JCLBean;
import com.shm.metro.bean.TrainData;
import com.shm.metro.core.TCPServer;
import com.shm.metro.enums.NotifyType;
import com.shm.metro.interfacer.IProtocol;
import com.shm.metro.observer.Observer;
import com.shm.metro.protocol.BroadcastProtocol;
import com.shm.metro.protocol.DataDecode;
import com.shm.metro.util.Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.shm.metro.util.Util.bytesToHexString;
import static com.shm.metro.util.Util.hex2bin;


public class TCPStreamDecoder extends ByteToMessageDecoder implements Observer{

    private List<Byte> readByte = new ArrayList<>();

    private BroadcastProtocol protocol;

    private JCLBean jclBean = null;

    private IProtocol iProtocol = null;//DynamicLoadJar.reloadFromLocal("/Users/lixiaohuan/IdeaProjects/parse/target/parse-1.0-SNAPSHOT-jar-with-dependencies.jar","com.shm.metro.parse.ShMetroMessageHeader");

    private long currentKey=0;

    private Logger logger = Logger.getLogger("messageLogger");

    private TCPServer tcpServer = null;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    Date eventDate = null;

    private boolean recordLog = Boolean.parseBoolean(Util.getConfig().getProperty("recored_log"));

    private AtomicBoolean needReloadClass = new AtomicBoolean(true);

    public TCPStreamDecoder(TCPServer tcpServer,BroadcastProtocol protocol) throws Exception {
        this.protocol = protocol;
        this.tcpServer = tcpServer;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.tcpServer.attach(this);
        logger.info("添加了"+this);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        this.tcpServer.detach(this);
        logger.info("移除了"+this);
        iProtocol = null;
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

    private IProtocol getiProtocolFromJCL(){
        try {
            jclBean = this.tcpServer.getJclBean();
            iProtocol = jclBean.getIProtocol();
            return iProtocol;
        } catch (Exception e) {
            logger.error("getiProtocolFromJCL error => "+ e.getMessage());
            e.printStackTrace();
            //return null;
        }
        return null;
    }

    protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
    {
        if(needReloadClass.get()) {
            iProtocol = this.getiProtocolFromJCL();
            needReloadClass.set(false);
        }

        if(iProtocol==null){
            return null;
        }

        byte[] msgHeader = new byte[iProtocol.getProtocolHeadMaxLength()];

        if(in.readableBytes() < iProtocol.getProtocolHeadMaxLength()) {
            if(logger.isInfoEnabled()) {
                logger.info("报头长度不够，重新读取！");
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
            //logger.info(msgHeader);
        }

        iProtocol.getProtocolHeadInfo(msgHeader);

        long datalen = iProtocol.getProtocollength()-iProtocol.getProtocolheadlength();

        //logger.info("train_no:"+iProtocol.getMarshalling());

        if (in.readableBytes() < datalen ) {
            for (int i = 0; i < msgHeader.length; i++) {
                readByte.add(msgHeader[i]);
            }
            return null;
        }
        TrainData traininfo = tcpServer.getTrainData().get(iProtocol.getMarshalling());

        //crc以及报尾
        int tailLen = iProtocol.getProtocoltaillength();
        //payload部分
        long bodyLen = datalen - tailLen;
        byte[] body = new byte[(int)bodyLen];
        in.readBytes(body);
        byte[] msgTail = new byte[tailLen];
        in.readBytes(msgTail);

        byte[] singlePackage = Util.concatAll(msgHeader,body,msgTail);

        iProtocol.analysisProtocol(singlePackage);

        DataDecode dataDecode = null;

        try {
            eventDate = simpleDateFormat.parse((iProtocol.getYear()+ 2000) + "-"+
                    iProtocol.getMonth()+"-"+
                    iProtocol.getDay()+" "+
                    iProtocol.getHour() + ":" +
                    iProtocol.getMinute() + ":" +
                    iProtocol.getSecond() + "." +
                    iProtocol.getMillisecond());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(iProtocol.getIsheartbeat()==0x02) {
            dataDecode = new DataDecode(traininfo, eventDate.getTime(), body,iProtocol.getFeedbackheartbeatmessage(),iProtocol.getCrcresult(),iProtocol.getIsheartbeat(),iProtocol.getResultSignals(),iProtocol.getVersion());
        }else{
            dataDecode = new DataDecode(traininfo, eventDate.getTime(), body, iProtocol.getFeedbackstatusmessage(), iProtocol.getCrcresult(), iProtocol.getIsheartbeat(), iProtocol.getResultSignals(), iProtocol.getVersion());
        }

        try {
            //默认该车号存kafka
            this.tcpServer.trainSaveKafka.put(dataDecode.getTrainData().getTrain_no(), true);
            //判断车号报文时间是否重复
            if(iProtocol.getIsheartbeat()==0x02) {
                saveToLog(dataDecode.getTrainData().getTrain_no(), eventDate.getTime(), singlePackage);
            }else if(this.tcpServer.trainResTime.containsKey(dataDecode.getTrainData().getTrain_no())){
                //如果不重复则存日志
                if(!this.tcpServer.trainResTime.get(dataDecode.getTrainData().getTrain_no()).contains(eventDate.getTime())){
                    saveToLog(dataDecode.getTrainData().getTrain_no(), eventDate.getTime(), singlePackage);
                    //正常报文则替换时间
                    if(this.tcpServer.trainResTime.get(dataDecode.getTrainData().getTrain_no()).size() >= 10){
                        //删除该车号第一个存的时间
                        this.tcpServer.trainResTime.get(dataDecode.getTrainData().getTrain_no()).remove(0);
                    }
                    this.tcpServer.trainResTime.get(dataDecode.getTrainData().getTrain_no()).add(eventDate.getTime());
                }else{
                    System.out.println("repeat data,train_no:" + dataDecode.getTrainData().getTrain_no() + ",time:" + eventDate.getTime() + "");
                    //如果重复则不存日志,并且不存到kafka
                    this.tcpServer.trainSaveKafka.put(dataDecode.getTrainData().getTrain_no(), false);
                }
            }else{
                LinkedList timeList = new LinkedList();
                timeList.add(eventDate.getTime());
                this.tcpServer.trainResTime.put(dataDecode.getTrainData().getTrain_no(), timeList);
                saveToLog(dataDecode.getTrainData().getTrain_no(), eventDate.getTime(), singlePackage);
            }
        }catch (Exception e){
            if(dataDecode.getTrainData()== null){
                logger.warn("iProtocol.getMarshalling():"+ iProtocol.getMarshalling() + " info is null");
            }
        }

        if(logger.isInfoEnabled()) {
            logger.info(traininfo);
        }

        return dataDecode;

    }


    private void saveToLog(String train_no,long time,byte[] buffer){
        if(recordLog) {
            logger.warn("receive data,train_no:" + train_no + ",time:" + time + ",value:" + bytesToHexString(buffer));
        }
    }

    private byte[] readByteFromByteBuf(ByteBuf buf) {
        //byte[] array = null;
        if (!buf.hasArray()) {            //1
            int length = buf.readableBytes();//2
            byte[] array = new byte[length];
            buf.getBytes(buf.readerIndex(), array);        //4
            return array;
        } else {
            return buf.array();
        }
    }

    @Override
    public void update(NotifyType notifyType) {
        switch (notifyType){
            case TRAIN_DATA:
                break;
            case CLASS_LOADER:
                needReloadClass.set(true);
                break;
            default:
                break;
        }
    }
}
