package com.jiapeng.messageplatform.entity;

public class ClassEntity {
    private String cl_code;
    private String cl_number;
    private String name;
    private String sc_code;
    private String gr_code;
    private String uuidStr;

    public String getUuidStr() {
        return uuidStr;
    }

    public void setUuidStr(String uuidStr) {
        this.uuidStr = uuidStr;
    }


    public String getCl_number() {
        return cl_number;
    }

    public void setCl_number(String cl_number) {
        this.cl_number = cl_number;
    }

    public String getCl_code() {
        return cl_code;
    }

    public void setCl_code(String cl_code) {
        this.cl_code = cl_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSc_code() {
        return sc_code;
    }

    public void setSc_code(String sc_code) {
        this.sc_code = sc_code;
    }

    public String getGr_code() {
        return gr_code;
    }

    public void setGr_code(String gr_code) {
        this.gr_code = gr_code;
    }
}
