package com.jiapeng.messageplatform.enums;

/**
 * Created by HZL on 2019/8/31.
 */
public enum MbMsgResEnum {
    WAITE(0, "等待发送"), SUCCESS(1, "提交成功"), REFUSE(2, "被拒绝"), FORMATERR(3, "数据格式错误"),
    MORESENDFAIL(4, "多次发送失败"), ZHENENDERR(5, "帧结束标志"), SERIALNUMERR(6, "序列号错误"),
    SYSREFUSESEND(7, "系统拒绝发送，可能是选择不到通道等情况");

    private int code;
    private String desc;

    MbMsgResEnum(int code, String desc) {
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

    public static MbMsgResEnum getType(Integer code) throws Exception {
        MbMsgResEnum defaultType = null;
        for (MbMsgResEnum type : MbMsgResEnum.values()) {
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
