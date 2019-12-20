package com.jiapeng.messageplatform.utils;

import java.io.File;

/**
 * Created by HZL on 2019/6/24.
 */
public class Constants {
    //用户openid by hzl 2019-9-9 16:15:53
    public static final String OPENID = "OPENID";
    //验证码
    public static final String VERIFY_CODE = "VERIFYCODE";
    //登录教师编号
    public static final String LOGIN_TEACHER_NO = "LOGINTEACHERNO";
    //登录教师学校代码
    public static final String LOGIN_TEACHER_SCCODE = "scCode";

    public static final String LOGIN_TEACHER_LOGINNAME = "LOGINNAME";

    public static final String LOGIN_TEACHER_OPENID = "LOGINTEACHEROPENID";





    //用户登录类型 by hzl 2019-8-22 15:41:27
    public static final String LOGIN_USER_TPYE = "LOGINUSERTPYE";

    public static final String USER_PASSWORD_ERROR = "用户名或密码错误";
    public static final String USER_PASSWORD_EMPTY = "用户名或密码不能为空";
    public static final String VERIFY_CODE_EMPTY = "验证码不能为空";
    public static final String VERIFY_CODE_ERROR = "验证码不正确";

    //门禁系统上传图片文件夹名称（前面不带斜杠/,后面带斜杠 File.separator）
    public static final String ASC_IMG_UPLOAD_PATH_NAME = "AscImage" + File.separator;


    //    微信公众号接口地址
    //微信授权地址获取
    public static final String GET_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
            "appid=[appid]&redirect_uri=[redirect_uri]&response_type=code" +
            "&scope=snsapi_userinfo&state=[state]&connect_redirect=1#wechat_redirect";
}

