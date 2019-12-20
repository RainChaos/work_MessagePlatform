package com.jiapeng.messageplatform.entity;

/**
 * Created by Administrator on 2019/10/30.
 */
public class AttendEntity {
    private String studentId;
    private String name;
    private String type;
    private String creatTime;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public AttendEntity(String studentId, String name, String type, String creatTime) {
        this.studentId = studentId;
        this.name = name;
        this.type = type;
        this.creatTime = creatTime;
    }
}
