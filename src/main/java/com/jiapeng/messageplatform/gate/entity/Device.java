package com.jiapeng.messageplatform.gate.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jiapeng.messageplatform.utils.MapTrunEntity;
import lombok.Data;

import java.util.Map;

/**
 * 设备参数
 * Created by HZL on 2019/12/9.
 */
@Data
public class Device {
    private String Device_AppID;//开发者应用ID mjf56780b5624dc7c8
    private String Device_No;//设备编号 5fc7540d5efdf555
    private String Device_Model;//设备型号 FACE20190111
    private String Device_Name;//设备名称 门禁1
    private String Device_Area;//安装位置 3号会议室门口
    private Integer Device_Type;//设备类型 [0,1]：0-门禁设备,1-人脸识别(默认)
    private Integer Device_Enable;//启用状态 [0,1]：0-禁用,1-启用(默认)
    private Integer Device_OutInType;//出入类型 [0,1]：0-出口(默认),1-入口
    private Integer Device_Blacklist;//是否启用黑名单 [0,1]：0-不启用,1-启用(默认)
    private Integer Device_PictureRatio;//图片压缩比例 [0-100]：默认80
    private String Device_Pwd;//设备密码 仅支持6位数字密码,不传则默认888888
    private Integer Device_Volume;//设备音量 [0-100]：默认80
    private Integer Device_LightBright;//设备补光灯亮度 [0-100]：默认100
    private Integer Device_LightMode;//设备补光灯控制方式 [0,1,2,3]：0-智能(默认),1-常开,2-常关,3-自定时间段
    private String Device_LightOpenTime;//设备补光灯开启时间 控制方式为3必填：yyyy-MM-dd HH:mm:ss
    private String Device_LightCloseTime;//设备补光灯关闭时间 控制方式为3必填：yyyy-MM-dd HH:mm:ss
    private String Device_RecogMode;//设备识别方式 FRC:人脸识别,PEC:人证比对,PEC_WHITE:加白名单(双重验证),IC_FRC:IC卡模式,格式如下： {"FRC":true,"PEC":false,"PEC_WHITE":false,"IC":false,"IC_FRC":false}
    private Integer Device_RecogThreshold;//名单阈值 [70-100]：默认85,数值越大识别精确度越高,识别耗时越久。
    private Integer Device_WhiteThreshold;//人证对比阈值 [30-100]：默认85,设备识别方式为白名单时优先级高于同于阈值。
    private Integer Device_OpenBroadcast;//设备是否开启语音播报 [0,1]：0-禁用,1-启用(默认)
    private Integer Device_RecogInterval;//设备识别间隔 [2-250]：单位秒(s),默认5s
    private Integer Device_RecogSpace;//设备识别距离 [0,50,100,200]：单位厘米,默认0(无限制)
    private Integer Device_DoorMode;//门禁控制方式 [0,1]：0-普通模式(默认),1-人工确认
    private Integer Device_DoorInterval;//门禁开门延时 [1-250]：单位秒(s),默认5s
    private Integer Device_DoorWiegandType;//韦根输出 [1,2]：1-虚拟卡(默认),2-ic/id卡
    private Integer Device_DoorWiegand;//韦根类型 [26,34,66]：默认26
    private Integer Device_OpenAdvert;//是否启用广告显示 [0,1]：0-禁用(默认),1-启用
    private Integer Device_AutoRecord;//识别记录自动上传 [0,1]：0-禁用(默认),1-启用
    private String Device_AutoRecordUrl;//识别记录上传地址 http://xxx.cpyht.com/record/upload
    private Integer Device_IsLiving;//活体识别 [0,1]：0-禁用(默认),1-启用
    private Integer Device_LivingThreshold;//活体识别阈值 [0-10]
    private Integer Device_Stranger;//陌生人识别参数 [0,2]：0-关闭,1-语音提示,2-语音提示加人脸抓怕
    private Integer Device_AdvertTime;//广告切换时间 [1-255],单位秒(s),默认5s
    private String Device_AdvertTitle;//广告标语
    private Integer Device_AutoOpenDoor;//识别后是否自动开闸 0-关闭,1-开启
    private Integer Device_RelayStatus;//继电器 0-常开,1-常关
    private Integer Device_ResatrtDay;//设备重启间隔(天) 取值范围[0,1,2,3,4,5,6]
    private Integer Device_resultSetting;//显示设置 [0,1]： 0-名字,1-自定义
    private String Device_ShowText;//自定义识别结果显示 {name}
    private Integer Device_voiceSetting;//语音设置 [0,2]： 0-默认,1-播报名字,2-自定义
    private String Device_BroadcastText;//自定义语音内容 {name}
    private String Device_ResatrtTime;//设备重启时间点 0.208333333333333
    private int Device_DelayAlamValue;//门磁报警延时 单位秒(s),取值0~1800,设置为0则为不报警,其它为报警 默认值：60

    //转换成json对象字符串
    public String toJsonStr() {
        return  JSON.toJSONString(this);
    }

    //转换成Map<String, Object>对象
    public Map<String, Object> toMap() {
        return MapTrunEntity.entityToMap(this);
    }
}
