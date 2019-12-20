package com.jiapeng.messageplatform.gate.code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jiapeng.messageplatform.utils.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by HZL on 2019/12/9.
 */
@Data
@Component
public class GateResponse {
    @Value("${gate.appsecret}")
    private String appSecret;

    private String code;//返回状态 1：成功,其他失败
    private String msg;//提示信息 设备已存在、新增成功等
    private String appid;//开发者应用ID mjf56780b5624dc7c8
    private String deviceno;//设备编号 5fc7540d5efdf555
    private String sign;//签名字符串 详情见签名算法
    private String timestemp;//时间戳 yyyyMMddHHmmssfff
    private Object data;//返回数据中data的参数 * 此接口中data返回空

    /**
     * 根据接口返回结果获取返回对象
     * by hzl 2019-12-9 16:58:04
     *
     * @param result
     * @return
     * @throws Exception
     */
    public GateResponse getResponse(String result) throws Exception {
        if (StringUtils.isBlank(result)) {
            throw new Exception("api result content is blank");
        }
        GateResponse gateResponse;
        try {
            gateResponse = JSON.parseObject(result, GateResponse.class);
        } catch (Exception e) {
            System.out.println(result);
            throw new Exception("api result content format error");
        }
        //以下代码注释说明：对返回结果sign暂时不校验，因为接口返回来的sign不懂是怎样计算的，对方说不用校验 by hzl 2019-12-11 14:55:13
//            //验签
//            String localSign = Md5Util.generateSignature(MapTrunEntity.entityToMap(gateResponse), appSecret);
//            if (null == gateResponse.getSign() || !gateResponse.getSign().equals(localSign)) {
//                throw new Exception("sign error");
//            }
        if (!gateResponse.getCode().equals("1")) {
            throw new Exception("gate api error:" + gateResponse.getMsg());
        }
        return gateResponse;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> mapTrun(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<String, String>();

        Set<String> keySet = map.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            Object value = map.get(k);
            sortMap.put(k, value == null ? "" : value.toString());
        }
        return sortMap;
    }

}
