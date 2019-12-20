package com.jiapeng.messageplatform.entity;

public class GradeEntity {
    private String sc_code;
    private String gr_code;
    private String name;
    private int orderNumber;

    private String uuidStr;

    public String getUuidStr() {
        return uuidStr;
    }

    public void setUuidStr(String uuidStr) {
        this.uuidStr = uuidStr;
    }

    public String getSc_code() {
        return sc_code;
    }

    public void setSc_code(String sc_code) {
        this.sc_code = sc_code;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getGr_code() {
        return gr_code;
    }

    public void setGr_code(String gr_code) {
        this.gr_code = gr_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
