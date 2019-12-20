package com.jiapeng.messageplatform.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.jiapeng.messageplatform.annotation.LoginLimit;
import com.jiapeng.messageplatform.utils.Constants;
import com.jiapeng.messageplatform.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 登录认证过滤器
 */
public class LoginInterceptor extends HandlerInterceptorAdapter{


    /**
     * 检查是否已经登录
     * by hzl 2019-8-22 16:23:03
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            String a = "";
            return super.preHandle(request, response, handler);
        }
        if (methodLoginLimit(handler)) {
            return true;
        }
        String servletPath = request.getServletPath();
        HandlerMethod method = (HandlerMethod) handler;
        //获取当前方法LoginLimit  Annotation
        LoginLimit methodAnnotation = method.getMethodAnnotation(LoginLimit.class);
        if (methodAnnotation == null) {
            //如果当前方法没有LoginLimit Annotation则获取控制器的LoginLimit  Annotation
            methodAnnotation = method.getMethod().getDeclaringClass().getAnnotation(LoginLimit.class);
        }
        //获取控制器类注解的用户类型
        String limitUserType = methodAnnotation == null ? null : methodAnnotation.type();
        HttpSession session = request.getSession();
        if (limitUserType != null) {
            switch (limitUserType) {
                case "teacher":
                    //获取教师登录的编号
                    String teNo = (String) session.getAttribute(Constants.LOGIN_TEACHER_NO);
                    if (StringUtils.isNotBlank(teNo)) {
                        return true;
                    } else {
                            response.setContentType("text/html;charset=gbk");
                            response.setCharacterEncoding("gbk");
                            response.setHeader("Cache-Control", "no-cache");
                        String html = "<script language='javascript'>top.layer.alert('登录超时，请重新登录！',{closeBtn: 0},function(index){window.open ('/login','_top');}); </script>";
                            response.getWriter().write(html);
                            return false;
                    }
                case "admin":
                    break;
                default:
                    return super.preHandle(request, response, handler);

            }
        }
        //如果没有注解的用户类型则两种用户都可能存在
        else {
            String path = request.getRequestURL().toString();
            if(path.contains("bindGuardian")
                    ||path.contains("schoolList")
                    ||path.contains("getGuardian")
                    ||path.contains("wxBand")
                    ||path.contains("cancelWxBand")
                    ||path.contains("getGuardianPhone")
                    ||path.contains("api")
                    ||path.contains("getMessageContent")
                    ||path.contains("messageDetails")
                    ||path.contains("ascNoticeInfo")
                    ||path.contains("showPic")
                    ||path.contains("images")
                    ||path.contains("imgUpload")
                    ||path.contains("bindPage")
                    ||path.contains("operate")
                    ||path.contains("att")
                    ||path.contains("app")
                    ||path.contains("score")


                    ){
                return true;
            }


            //获取管理员
            String name = (String) session.getAttribute("name");
            String teNo = (String) session.getAttribute(Constants.LOGIN_TEACHER_NO);

            if (StringUtils.isNotBlank(name)||StringUtils.isNotBlank(teNo)) {
                return true;
            } else {
                    response.setContentType("text/html;charset=gbk");
                    response.setCharacterEncoding("gbk");
                    response.setHeader("Cache-Control", "no-cache");
                String html = "<script language='javascript'>top.layer.alert('登录超时，请重新登录！',{closeBtn: 0},function(index){window.open ('/login','_top');}); </script>";
                    response.getWriter().write(html);
                    return false;



            }

        }
        return super.preHandle(request, response, handler);
    }


    //判断是否有limit()限制
    public boolean methodLoginLimit(Object handler) {
        HandlerMethod method = (HandlerMethod) handler;
        //获取当前方法PermessionLimit
        LoginLimit loginLimit = method.getMethodAnnotation(LoginLimit.class);
        if (loginLimit == null) {
            //获取控制器的PermessionLimit
            loginLimit = method.getMethod().getDeclaringClass().getAnnotation(LoginLimit.class);
        }
        if (loginLimit != null && !loginLimit.limit()) {
            return true;
        } else {
            return false;
        }
    }

    public void output(String jsonStr, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String header = request.getHeader("Origin");
        response.setContentType("text/html;charset=UTF-8;");
        response.setHeader("Access-Control-Allow-Origin", header);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE,token");
        PrintWriter out = response.getWriter();
        out.println(jsonStr);
        out.flush();
        out.close();
    }

}
