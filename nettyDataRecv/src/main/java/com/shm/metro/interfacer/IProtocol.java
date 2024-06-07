package com.shm.metro.interfacer;

import java.util.Map;

/**
 * Created by sunhaijian on 2023/9/5.
 */
public interface IProtocol {
    int  getProtocolHeadMaxLength();
    void getProtocolHeadInfo(byte[] head);

    void analysisProtocol(byte[] protocol);

    public byte getIsheartbeat() ;

    public byte[] getFeedbackheartbeatmessage() ;

    public byte[] getFeedbackstatusmessage() ;

    public int getProtocollength() ;

    public String getMarshalling() ;

    public String getVersion() ;

    public int getYear() ;

    public int getMonth();

    public int getDay() ;

    public int getHour() ;

    public int getMinute() ;

    public int getSecond() ;

    public int getMillisecond() ;

    public int getProtocolid();

    public byte getTimetype() ;

    public long getTimestamp() ;

    public int getProtocolheadlength() ;

    public int getProtocoltaillength() ;

    public String getMessagehead() ;

    public String getMessagetail() ;

    public byte[] getProtocolhead() ;

    public byte[] getProtocoltail() ;

    public byte[] getData() ;

    public byte getCrcresult() ;

    public Map getResultSignals();

}
