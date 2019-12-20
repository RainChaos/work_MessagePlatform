package com.jiapeng.messageplatform.gate.code;

import com.alibaba.fastjson.JSON;
import com.jiapeng.messageplatform.utils.Md5Util;
import com.jiapeng.messageplatform.utils.VeDate;
import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

/**
 * 请求参数实体
 * Created by HZL on 2019/12/9.
 */
@Data
public class GateRequestParams {

    private Map<String, Object> paramsmap = new TreeMap<>();

    public GateRequestParams() {

    }

    public GateRequestParams(String appid) {
        String timeStemp = VeDate.getLocalDate("yyyyMMddHHmmssSSS");
        paramsmap.put("timestemp", timeStemp);
        paramsmap.put("appid", appid);
    }

    public void setValue(String key, Object value) {
        paramsmap.put(key, value);
    }

    public Object getValue(String key) {
        Object o = null;
        o = paramsmap.get(key);
        return o;
    }

    public void removeKey(String key) {
        if (paramsmap.containsKey(key)) {
            paramsmap.remove(key);
        }
    }

    //闸机sign字段加密方法
    public String makeSign(String appSecret) {
        String sign = Md5Util.generateSignature(paramsmap, appSecret);
        return sign;
    }

    public String getJsonStr() {
        String josnStr = JSON.toJSONString(paramsmap);
        return josnStr;
    }
}
