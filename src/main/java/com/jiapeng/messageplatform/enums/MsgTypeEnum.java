package com.jiapeng.messageplatform.enums;

/**
 * 消息类型
 * Created by HZL on 2019/8/24.
 */
public enum MsgTypeEnum {
    WECHAT(1, "微信消息"), MOBILE(2, "手机短信");

    private int code;
    private String desc;

    MsgTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static MsgTypeEnum getType(Integer code) throws Exception {
        MsgTypeEnum defaultType = null;
        for (MsgTypeEnum type : MsgTypeEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        if (defaultType == null)
            throw new Exception("notiway类型未知：" + code);
        return defaultType;
    }

    public static String getDescByCode(Integer code) throws Exception {
        return getType(code).desc;
    }
}
