package com.jiapeng.messageplatform.utils;


import com.alibaba.fastjson.JSON;
import com.jiapeng.messageplatform.gate.code.MD5;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * Created by ly on 2016/11/8.
 */
public class Md5Util {
    public final static String MD5(String inStr) {
        try {
            byte[] btInput = inStr.getBytes("utf-8");
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }


    public static void main(String[] args) {
//        String signStr="appid=mjcc70044f73242483&data={\"personnel_GroupName\":\"123\",\"personnel_IDCard\":\"123456789123456785\",\"personnel_Name\":\"moon5\",\"personnel_No\":\"0005\"}&timestemp=20191219123209948&key=30ded7c5f5f34334800184bfc0bcd834";
        String signStr = "chinasaas都是我";
        System.out.println(MD5(signStr).toUpperCase());
        System.out.println(com.jiapeng.messageplatform.gate.code.MD5.md5(signStr).toUpperCase());
    }

    //对参数进行签名
    //◆参数名ASCII码从小到大排序（字典序）；
    //◆如果参数的值为空不参与签名；
    //◆参数名区分大小写；
    //◆验证调用返回或微信主动通知签名时，传送的sign参数不参与签名，将生成的签名与该sign值作校验。
    public static String generateSignature(final Map<String, Object> data, String key) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            Object value = data.get(k);
            if (null != value && value.toString().trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(value.toString().trim()).append("&");
        }
        sb.append("key=").append(key);
        String res = sb.toString();
        return MD5(res).toUpperCase();
    }

    public static String generateSignature1(final Map<String, Object> data, String key) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            Object value = data.get(k);
            if (null != value && value.toString().trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(value.toString().trim()).append("&");
        }
        sb.append("key=").append(key);
        String res = sb.toString();
        return com.jiapeng.messageplatform.gate.code.MD5.md5(res).toUpperCase();
    }
}
