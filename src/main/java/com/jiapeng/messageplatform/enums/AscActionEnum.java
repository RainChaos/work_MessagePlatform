package com.jiapeng.messageplatform.enums;

/**
 * 门禁进出动作状态
 * 包括正常的出入、请假的出入状态
 * Created by HZL on 2019/8/24.
 */
public enum AscActionEnum {
    NORMALLEAVE(0, "离开"), NORMALINTO(1, "进入"),
    LEAVEONLEAVE(2, "离开（请假）"), LEAVEONINTO(3, "回到（请假）"),
    LATEINTO(4, "回到（迟到）");

    private int code;
    private String desc;

    AscActionEnum(int code, String desc) {
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

    public static AscActionEnum getType(Integer code) throws Exception {
        AscActionEnum defaultType = null;
        for (AscActionEnum type : AscActionEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        if (defaultType == null)
            throw new Exception("AscAction类型未知：" + code);
        return defaultType;
    }

    public static String getDescByCode(Integer code) throws Exception {
        return getType(code).desc;
    }
}
