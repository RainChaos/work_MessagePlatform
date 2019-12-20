package com.jiapeng.messageplatform.entity;

import com.jiapeng.messageplatform.utils.VeDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Leave {
    private Integer id;

    private String teNo;

    private String stuNo;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private String reason;

    private Date createDate;

    private Date updateDate;

    private Boolean gateReadState;

    private String readFailReason;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeNo() {
        return teNo;
    }

    public void setTeNo(String teNo) {
        this.teNo = teNo;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Boolean getGateReadState() {
        return gateReadState;
    }

    public void setGateReadState(Boolean gateReadState) {
        this.gateReadState = gateReadState;
    }

    public String getReadFailReason() {
        return readFailReason;
    }

    public void setReadFailReason(String readFailReason) {
        this.readFailReason = readFailReason;
    }

    //    获取请假时间以及原因组合信息
    public String getLeaveDateAndReason() {
        String leaveDate = "请假时间：" + VeDate.dateToStrLong(this.startDate) + " 至 " + VeDate.dateToStrLong(this.endDate);
        String leaveReason = "请假原因：" + this.reason;
        return leaveDate + "\n" + leaveReason;
    }

    //cun存储信息
    private String cl_code;
    private String name;
    private String stuName;
    private String sex;


    public String getCl_code() {
        return cl_code;
    }

    public String getName() {
        return name;
    }

    public String getStuName() {
        return stuName;
    }

    public String getSex() {
        return sex;
    }

    public void setCl_code(String cl_code) {
        this.cl_code = cl_code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

//
//    //<result column="cl_code" jdbcType="NVARCHAR" property="cl_code" />
//    <result column="name" jdbcType="NVARCHAR" property="name" />
//    <result column="stuName" jdbcType="NVARCHAR" property="stuName" />
//    <result column="sex" jdbcType="NVARCHAR" property="sex" />
}
