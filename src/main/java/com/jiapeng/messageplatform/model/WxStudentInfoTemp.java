package com.jiapeng.messageplatform.model;

import java.util.List;

/**
 * 用于MessageInfo使用(微信模板所需要用到的学生信息)
 * Created by HZL on 2019/8/14.
 */
public class WxStudentInfoTemp {
    String schoolName;//学校名称
    String gradeName;//年级名称
    String className;//班级名称
    String studentName;//学生姓名
    List<MsgTargetInfo> msgTargetInfoList;//家长opendId集合

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public List<MsgTargetInfo> getMsgTargetInfoList() {
        return msgTargetInfoList;
    }

    public void setMsgTargetInfoList(List<MsgTargetInfo> msgTargetInfoList) {
        this.msgTargetInfoList = msgTargetInfoList;
    }
}