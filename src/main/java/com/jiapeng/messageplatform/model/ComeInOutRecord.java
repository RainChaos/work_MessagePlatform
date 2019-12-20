package com.jiapeng.messageplatform.model;

import com.jiapeng.messageplatform.enums.AscActionEnum;
import com.jiapeng.messageplatform.enums.AscTypeEnum;
import com.jiapeng.messageplatform.utils.VeDate;

import java.util.Date;

/**
 * 考勤出入记录实体
 * Created by HZL on 2019/10/8.
 */
public class ComeInOutRecord {
    private String stuNo;//学号
    private String stuName;//姓名
    private String className;//班级
    private String gradeName;//年级
    private String schoolName;//学校
    private int ascType;//门禁类型
    private int actionType;//出入方向
    private String ascTypeStr;//门禁类型
    private String actionTypeStr;//出入方向
    private Date actionTime;//出入时间
    private String actionTimeStr;//出入时间
    private String actionState;//出入状态（正常、请假、迟到）
    private String inOutType;//出入方向（进入、离开）
    private int leaveId;//请假ID
    private String noticeResult;//推送结果
    private String reason;//失败原因

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getAscType() {
        return ascType;
    }

    public void setAscType(int ascType) {
        this.ascType = ascType;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getAscTypeStr() {
        try {
            return AscTypeEnum.getDescByCode(ascType);
        } catch (Exception e) {
            return "";
        }
    }

    public void setAscTypeStr(String ascTypeStr) {
        this.ascTypeStr = ascTypeStr;
    }

    public String getActionTypeStr() {
        try {
            return AscActionEnum.getDescByCode(actionType);
        } catch (Exception e) {
            return "";
        }
    }

    public void setActionTypeStr(String actionTypeStr) {
        this.actionTypeStr = actionTypeStr;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public String getActionTimeStr() {
        if (actionTime != null) {
            return VeDate.dateToStrLong(actionTime);
        }
        return actionTimeStr;
    }

    public void setActionTimeStr(String actionTimeStr) {
        this.actionTimeStr = actionTimeStr;
    }

    public String getActionState() {
        return actionState;
    }

    public void setActionState(String actionState) {
        this.actionState = actionState;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    public String getNoticeResult() {
        return noticeResult;
    }

    public void setNoticeResult(String noticeResult) {
        this.noticeResult = noticeResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getInOutType() {
        return inOutType;
    }

    public void setInOutType(String inOutType) {
        this.inOutType = inOutType;
    }

    public ComeInOutRecord(String stuNo, String stuName, String className, String gradeName, String schoolName,String actionTimeStr,String reason) {
        this.stuNo = stuNo;
        this.stuName = stuName;
        this.className = className;
        this.gradeName = gradeName;
        this.schoolName = schoolName;
        this.actionTimeStr = actionTimeStr;
        this.reason = reason;
    }
}
