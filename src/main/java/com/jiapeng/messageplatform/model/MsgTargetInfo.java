package com.jiapeng.messageplatform.model;

/**
 * Created by HZL on 2019/8/15.
 */
public class MsgTargetInfo {
    Integer detailId;//通知详情id
    String no;//通知号码
    Boolean isTeacher;//通知人是否为老师

    public MsgTargetInfo() {
    }

    public MsgTargetInfo(Integer detailId, String no) {
        this.detailId = detailId;
        this.no = no;
        this.isTeacher = false;
    }

    public MsgTargetInfo(Integer detailId, String no, Boolean isTeacher) {
        this.detailId = detailId;
        this.no = no;
        this.isTeacher = isTeacher;
    }

    public Integer getDetailId() {
        return detailId;
    }

    public void setDetailId(Integer detailId) {
        this.detailId = detailId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Boolean getTeacher() {
        return isTeacher;
    }

    public void setTeacher(Boolean teacher) {
        isTeacher = teacher;
    }
}
