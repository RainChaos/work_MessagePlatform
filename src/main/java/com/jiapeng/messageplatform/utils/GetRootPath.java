package com.jiapeng.messageplatform.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HZL on 2019/5/22.
 */
@Configuration
public class GetRootPath {

    public List<String> MobilRegex = new ArrayList<>();

    @Value("${config.regex.mobil}")
    public String MobilRegexStr;
    
    public List<String> getMobilRegex() {
        String[] tmp = MobilRegexStr.split("\\|");
        for (String str : tmp) {
            MobilRegex.add(str);
        }
        return MobilRegex;
    }

    /**
     * 判断手机浏览器的正则表达式
     */
    public String getPath(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        for (String str : getMobilRegex()) {
            if (userAgent.indexOf(str) != -1) {
                return "mobile";
            }
        }
        return "classic";
    }
}