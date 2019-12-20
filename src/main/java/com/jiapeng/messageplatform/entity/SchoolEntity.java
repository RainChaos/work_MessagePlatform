package com.jiapeng.messageplatform.entity;

import com.jiapeng.messageplatform.utils.TokenProccessor;

import java.util.Date;
import java.util.UUID;

public class SchoolEntity {
    private String sc_code;
    private String name;
    private String token= TokenProccessor.getInstance().makeToken();
    private String address;
    private String contacts;
    private String contactPhone;
    private Date createTime=new Date();
    private boolean status;
    private Date statusTime;
    private String statusReason;
    private String menhuKey;
    private String uuidStr;

    public String getUuidStr() {
        return uuidStr;
    }

    public void setUuidStr(String uuidStr) {
        this.uuidStr = uuidStr;
    }

    public String getMenhuKey() {
        return menhuKey;
    }

    public void setMenhuKey(String menhuKey) {
        this.menhuKey = menhuKey;
    }

    public String getSc_code() {
        return sc_code;
    }

    public void setSc_code(String sc_code) {
        this.sc_code = sc_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(Date statusTime) {
        this.statusTime = statusTime;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }
}
