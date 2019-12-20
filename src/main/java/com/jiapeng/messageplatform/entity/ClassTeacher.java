package com.jiapeng.messageplatform.entity;

/**
 * Created by HZL on 2019/8/7.
 */
public class ClassTeacher {
    private String clCode;
    private String teNo;
    private Boolean isHeadMaster;

    public String getClCode() {
        return clCode;
    }

    public void setClCode(String clCode) {
        this.clCode = clCode;
    }

    public String getTeNo() {
        return teNo;
    }

    public void setTeNo(String teNo) {
        this.teNo = teNo;
    }

    public Boolean getHeadMaster() {
        return isHeadMaster;
    }

    public void setHeadMaster(Boolean headMaster) {
        isHeadMaster = headMaster;
    }
}
