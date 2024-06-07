package com.shm.metro.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2023/8/30.
 */
public class TrainData implements Serializable {
    private static final long serialVersionUID = 2187171405467993594L;
    private int id;
    private String train_no;
    private String train_name;
    private String train_type_no;
    private String train_type_name;
    private String train_body_type;
    private int line_id;
    private String line_name;
    private int supplier_id;
    private String wheeltrack_type;
    private Date online_time;
    private String ratio;
    private int protocol_id;

    public String getLine_name() {
        return line_name;
    }

    public void setLine_name(String line_name) {
        this.line_name = line_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrain_type_no() {
        return train_type_no;
    }

    public void setTrain_type_no(String train_type_no) {
        this.train_type_no = train_type_no;
    }

    public String getTrain_no() {
        return train_no;
    }

    public void setTrain_no(String train_no) {
        this.train_no = train_no;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getTrain_type_name() {
        return train_type_name;
    }

    public void setTrain_type_name(String train_type_name) {
        this.train_type_name = train_type_name;
    }

    public String getTrain_body_type() {
        return train_body_type;
    }

    public void setTrain_body_type(String train_body_type) {
        this.train_body_type = train_body_type;
    }

    public int getLine_id() {
        return line_id;
    }

    public void setLine_id(int line_id) {
        this.line_id = line_id;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getWheeltrack_type() {
        return wheeltrack_type;
    }

    public void setWheeltrack_type(String wheeltrack_type) {
        this.wheeltrack_type = wheeltrack_type;
    }

    public Date getOnline_time() {
        return online_time;
    }

    public void setOnline_time(Date online_time) {
        this.online_time = online_time;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public int getProtocol_id() {
        return protocol_id;
    }

    public void setProtocol_id(int protocol_id) {
        this.protocol_id = protocol_id;
    }

    @Override
    public String toString() {
        return "TrainData{" +
                "id=" + id +
                ", train_no='" + train_no + '\'' +
                ", train_name='" + train_name + '\'' +
                ", train_type_no='" + train_type_no + '\'' +
                ", train_type_name='" + train_type_name + '\'' +
                ", train_body_type='" + train_body_type + '\'' +
                ", line_id=" + line_id +
                ", supplier_id=" + supplier_id +
                ", wheeltrack_type='" + wheeltrack_type + '\'' +
                ", online_time=" + online_time +
                ", ratio='" + ratio + '\'' +
                ", protocol_id=" + protocol_id +
                '}';
    }
}
