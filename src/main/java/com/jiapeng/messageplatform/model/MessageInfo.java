package com.jiapeng.messageplatform.model;

import java.io.Serializable;
import java.util.List;

/**
 * 提供给电信的短信消息格式实体
 * Created by HZL on 2019/8/6.
 */
public class MessageInfo implements Serializable {
    private String scCode;//学校代码
    private String msgNo; //MessageLogId
    private String batchId; //uuid格式的批次号
    private String content; //消息内容
    private String teName; //发布者（教师姓名）
    private String sendDate; //发送时间
    private List<WxStudentInfoTemp> wxStudentInfoTempList; //待发送手机号码或微信openid集合

    public String getScCode() {
        return scCode;
    }

    public void setScCode(String scCode) {
        this.scCode = scCode;
    }

    public String getMsgNo() {
        return msgNo;
    }

    public void setMsgNo(String msgNo) {
        this.msgNo = msgNo;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getTeName() {
        return teName;
    }

    public void setTeName(String teName) {
        this.teName = teName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public List<WxStudentInfoTemp> getWxStudentInfoTempList() {
        return wxStudentInfoTempList;
    }

    public void setWxStudentInfoTempList(List<WxStudentInfoTemp> wxStudentInfoTempList) {
        this.wxStudentInfoTempList = wxStudentInfoTempList;
    }

}
