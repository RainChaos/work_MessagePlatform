package com.jiapeng.messageplatform.entity;

import java.util.List;

/**
 * Created by Administrator on 2019/9/23.
 */
public class LeaveTotal {
    private String stuNo;
    private String stuName;
    private String leaveTotal;


    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public void setLeaveTotal(String leaveTotal) {
        this.leaveTotal = leaveTotal;
    }

    public String getStuName() {
        return stuName;
    }

    public String getLeaveTotal() {
        return leaveTotal;
    }
}
