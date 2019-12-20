package com.jiapeng.messageplatform.gate.entity;

import lombok.Data;

/**
 * 识别记录信息实体
 * Created by HZL on 2019-12-18.
 */
@Data
public class AutoRecord {
    private String id;//UUID
    private String time;//比对时间
    private String name;//姓名
    private String sex;//性别
    private String nation;//民族
    private String idNum;//身份证号码
    private String birthday;//生日
    private String address;//地址
    private String depart;//签发机关
    private String validStart;//有效期开始
    private String validEnd;//有效期结束
    private String mac;//设备MAC地址
    private String inout;//进出类型
    private String openType;//开门方式
    private String resultStatus;//识别结果
    private String scenePhoto;//现场抓拍图片 （Base64格式）
    private String userPhoto;//白名单图片（Base64格式）
}
