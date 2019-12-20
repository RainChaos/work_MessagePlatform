package com.jiapeng.messageplatform.entity;

import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class UserEntity  implements Serializable {
    private int id;
    private String name;
    private String passWord;
    private boolean disLog=false;
    private String disLogReason;
    private Date disLogTime ;
    private String scCode;
    private String details;

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public UserEntity(){super();}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScCode() {
        return scCode;
    }

    public void setScCode(String scCode) {
        this.scCode = scCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean isDislog() {
        return disLog;
    }

    public void setDislog(boolean dislog) {
        this.disLog = dislog;
    }

    public String getDislogReason() {
        return disLogReason;
    }

    public void setDislogReason(String dislogReason) {
        this.disLogReason = dislogReason;
    }

    public Date getDisTime() {
        return disLogTime;
    }

    public void setDisTime(Date disTime) {
        this.disLogTime = disTime;
    }
}
