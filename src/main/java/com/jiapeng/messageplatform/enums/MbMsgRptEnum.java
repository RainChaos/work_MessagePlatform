package com.jiapeng.messageplatform.enums;

/**
 * Created by HZL on 2019/8/31.
 */
public enum MbMsgRptEnum {
    SUCCESS(0, "成功"), EXPIRED(1, "EXPIRED"), UNDELIVERABLE(2, "UNDELIVERABLE"), REJECTED(3, "REJECTED"),
    UNKNOWN(4, "UNKNOWN"), DELETED(5, "DELETED");

    private int code;
    private String desc;

    MbMsgRptEnum(int code, String desc) {
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

    public static MbMsgRptEnum getType(Integer code) throws Exception {
        MbMsgRptEnum defaultType = null;
        for (MbMsgRptEnum type : MbMsgRptEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        if (defaultType == null)
            throw new Exception("类型未知：" + code);
        return defaultType;
    }

    public static String getDescByCode(Integer code) throws Exception {
        return getType(code).desc;
    }
}
