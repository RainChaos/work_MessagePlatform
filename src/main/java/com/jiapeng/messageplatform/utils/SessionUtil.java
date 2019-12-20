package com.jiapeng.messageplatform.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by HZL on 2019/8/7.
 */
public class SessionUtil {
    public static void setSession(HttpServletRequest request, String key, String value) {
        HttpSession session = request.getSession();
        session.setAttribute(key, value);
    }

    public static Object getSession(HttpServletRequest request, String key) {
        HttpSession session = request.getSession();
        return session.getAttribute(key);
    }

    public static String getLoginTeacherNo(HttpServletRequest request) {
        Object object = getSession(request, Constants.LOGIN_TEACHER_NO);
        if (null != object && StringUtils.isNotBlank(object.toString())) {
            return object.toString();
        } else {
            return null;
        }
    }

    public static String getVerifyCode(HttpServletRequest request) {
        Object object = getSession(request, Constants.VERIFY_CODE);
        if (null != object && StringUtils.isNotBlank(object.toString())) {
            return object.toString();
        } else {
            return null;
        }
    }

}
