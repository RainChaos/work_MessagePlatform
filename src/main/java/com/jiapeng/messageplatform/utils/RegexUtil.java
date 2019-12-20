package com.jiapeng.messageplatform.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基础的正则表达式
 * Created by HZL on 2019/5/29.
 */
public class RegexUtil {

    //长度8-16位，且由数字和字母组成
    public static final String PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
    public static String EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    public static String MOBILE = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$";
    //弱密码类型
    //匹配6位顺增或顺降
    public static String PWDREGEX1 = "(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){5}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){5})\\d";
    //匹配4-9位连续的数字
    public static String PWDREGEX2 = "(?:(?:0(?=1)|1(?=2)|2(?=3)|3(?=4)|4(?=5)|5(?=6)|6(?=7)|7(?=8)|8(?=9)){3,}|(?:9(?=8)|8(?=7)|7(?=6)|6(?=5)|5(?=4)|4(?=3)|3(?=2)|2(?=1)|1(?=0)){3,})\\d";
    //匹配3位以上的重复数字
    public static String PWDREGEX3 = "([\\d])\\1{2,}";
    //匹配33111类型的
    public static String PWDREGEX4 = "([\\d])\\1{1,}([\\d])\\2{2,}";
    //匹配5331533类型的
    public static String PWDREGEX5 = "(([\\d]){1,}([\\d]){1,})\\1{1,}";
    //匹配22334,123355类型的
    public static String PWDREGEX6 = "([\\d])\\1{1,}([\\d])\\2{1,}";

    public static boolean isMatch(String regex, String content) {
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regex);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        // 字符串是否与正则表达式相匹配
        boolean rs = matcher.matches();
        return rs;
    }

    public static boolean isPwd(String content) {
        boolean flag = false;
        if (StringUtils.isNotBlank(content)) {
            flag = isMatch(PASSWORD, content);
        }
        return flag;
    }

    public static boolean isEmail(String content) {
        boolean flag = false;
        if (StringUtils.isNotBlank(content)) {
            flag = isMatch(EMAIL, content);
        }
        return flag;
    }

    public static boolean isMobile(String content) {
        boolean flag = false;
        if (StringUtils.isNotBlank(content)) {
            flag = isMatch(MOBILE, content);
        }
        return flag;
    }

    public static boolean isWeakPwd(String password) {
        boolean flag = false;
        if (isMatch(PWDREGEX1, password) || isMatch(PWDREGEX2, password) || isMatch(PWDREGEX3, password) || isMatch(PWDREGEX4, password) || isMatch(PWDREGEX5, password) || isMatch(PWDREGEX6, password)) {
            flag = true;
        }
        return flag;
    }

    public static void main(String[] asd) {
        String content = "1234567";
        System.out.println(isWeakPwd(content));
    }
}

