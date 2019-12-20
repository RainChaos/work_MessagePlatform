package com.jiapeng.messageplatform.gate.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jiapeng.messageplatform.utils.MapTrunEntity;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 人员参数
 * Created by HZL on 2019/12/9.
 */
@Data
public class Personnel {
    private String Personnel_Name;//姓名 张三
    private String Personnel_No;//人员编号 5DBB2AA5ABDFC3C6
    private String Personnel_IDCard;//身份证号 110956199905052000
    private Integer Personnel_Status;//审核状态 0-待审核,1-编辑,2-审核成功,3-已拒绝
    private String Personnel_Nation;//民族 汉族
    private Date Personnel_StartDate;//有效期起 43472
    private Integer Personnel_Type;//人员类型 1-白名单,2-黑名单,3-身份证,4-IC卡,5-未知
    private Integer Personnel_Sex;//性别 1-男,2-女
    private Date Personnel_EffDate;//有效期止 43472
    private Integer Personnel_Enable;//启用状态 0-禁用,1-启用,2-已删除
    private String Personnel_Photo;//人员照片 UrlEncode(Base64字符串)
    private String Personnel_CardNo;//卡号 55666236897412
    private String Personnel_GroupName;//分组名称 研发组
    private String Personnel_Phone;//手机号码 13800138000
    private String Personnel_IDCardAdd;//身份证地址 深圳
    private Date Personnel_IDCardExp;//身份证有效期止 43472
    private Date Personnel_IDCardEff;//身份证有效期起 43472
    private String Personnel_IDCardIsSue;//身份证签发机关 深圳
    private Date Personnel_UpdateTime;//最后修改时间 43472
    private Date Personnel_AddTime;//创建时间 43472

    //转换成json对象字符串
    public String toJsonStr() {
        return JSON.toJSONString(this);
    }

    //转换成Map<String, Object>对象
    public Map<String, Object> toMap() {
        return MapTrunEntity.entityToMap(this);
    }
}
