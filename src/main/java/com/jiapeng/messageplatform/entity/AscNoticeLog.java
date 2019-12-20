package com.jiapeng.messageplatform.entity;

import java.util.Date;

public class AscNoticeLog {
    private Integer id;

    private String scCode;

    private String stuId;

    private String noticeway;

    private String ascdeviceno;

    private Integer asctype;

    private Integer actiontype;

    private Date actiontime;

    private String ascremark;

    private String noticeresult;

    private String failreason;

    private Date createtime;

    private String imgName;

    private Integer leaveId;

    private String batchId;
    ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScCode() {
        return scCode;
    }

    public void setScCode(String scCode) {
        this.scCode = scCode;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getNoticeway() {
        return noticeway;
    }

    public void setNoticeway(String noticeway) {
        this.noticeway = noticeway;
    }


    public String getAscdeviceno() {
        return ascdeviceno;
    }

    public void setAscdeviceno(String ascdeviceno) {
        this.ascdeviceno = ascdeviceno;
    }

    public Integer getAsctype() {
        return asctype;
    }

    public void setAsctype(Integer asctype) {
        this.asctype = asctype;
    }

    public Integer getActiontype() {
        return actiontype;
    }

    public void setActiontype(Integer actiontype) {
        this.actiontype = actiontype;
    }

    public Date getActiontime() {
        return actiontime;
    }

    public void setActiontime(Date actiontime) {
        this.actiontime = actiontime;
    }

    public String getAscremark() {
        return ascremark;
    }

    public void setAscremark(String ascremark) {
        this.ascremark = ascremark;
    }

    public String getNoticeresult() {
        return noticeresult;
    }

    public void setNoticeresult(String noticeresult) {
        this.noticeresult = noticeresult;
    }

    public String getFailreason() {
        return failreason;
    }

    public void setFailreason(String failreason) {
        this.failreason = failreason;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public Integer getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(Integer leaveId) {
        this.leaveId = leaveId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }
}