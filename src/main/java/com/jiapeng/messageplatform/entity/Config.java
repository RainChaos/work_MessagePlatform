package com.jiapeng.messageplatform.entity;

public class Config {
    private Integer id;

    private Integer cfDeId;

    private String scCode;

    private String value;

    private String scName;

    private String cfName;

    private String cfCode;

    private String cfType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCfDeId() {
        return cfDeId;
    }

    public void setCfDeId(Integer cfDeId) {
        this.cfDeId = cfDeId;
    }

    public String getScCode() {
        return scCode;
    }

    public void setScCode(String scCode) {
        this.scCode = scCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getScName() {
        return scName;
    }

    public void setScName(String scName) {
        this.scName = scName;
    }

    public String getCfName() {
        return cfName;
    }

    public void setCfName(String cfName) {
        this.cfName = cfName;
    }

    public String getCfCode() {
        return cfCode;
    }

    public void setCfCode(String cfCode) {
        this.cfCode = cfCode;
    }

    public String getCfType() {
        return cfType;
    }

    public void setCfType(String cfType) {
        this.cfType = cfType;
    }
}