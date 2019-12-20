package com.jiapeng.messageplatform.entity;

import com.jiapeng.messageplatform.model.ComeInOutRecord;

import java.util.Date;

/**
 * Created by Administrator on 2019/10/30.
 */
public class AttendDetailsEntity extends ComeInOutRecord {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AttendDetailsEntity(String stuNo, String stuName, String className, String gradeName, String schoolName, String actionTimeStr, String reason, String type) {
        super(stuNo, stuName, className, gradeName, schoolName, actionTimeStr, reason);
        this.type = type;
    }
}
