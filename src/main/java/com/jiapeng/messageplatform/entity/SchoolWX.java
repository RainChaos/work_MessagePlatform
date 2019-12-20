package com.jiapeng.messageplatform.entity;

import java.util.Date;

public class SchoolWX {
    private String scCode;

    private String appId;

    private String appSecret;

    private String normalTmp;

    private String ascTmp;

    private Date expiresTime;

    private String token;

    public String getScCode() {
        return scCode;
    }

    public void setScCode(String scCode) {
        this.scCode = scCode;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getNormalTmp() {
        return normalTmp;
    }

    public void setNormalTmp(String normalTmp) {
        this.normalTmp = normalTmp;
    }

    public String getAscTmp() {
        return ascTmp;
    }

    public void setAscTmp(String ascTmp) {
        this.ascTmp = ascTmp;
    }

    public Date getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(Date expiresTime) {
        this.expiresTime = expiresTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}