package com.jiapeng.messageplatform.enums;

/**
 * 门禁类型
 * Created by HZL on 2019/8/24.
 */
public enum AscTypeEnum {
    SCHOOL(1, "校园"), DOR(2, "公寓");
    private int code;
    private String desc;

    AscTypeEnum(int code, String desc) {
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

    public static AscTypeEnum getType(Integer code) throws Exception {
        AscTypeEnum defaultType = null;
        for (AscTypeEnum type : AscTypeEnum.values()) {
            if (type.code == code) {
                return type;
            }
        }
        if (defaultType == null)
            throw new Exception("AscType类型未知：" + code);
        return defaultType;
    }

    public static String getDescByCode(Integer code) throws Exception {
        return getType(code).desc;
    }

}
