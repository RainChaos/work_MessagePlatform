package com.jiapeng.messageplatform.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 区分页面请求、接口请求，发送错误时，返回对应的错误格式
 * Created by HZL on 2019/7/1.
 */
public class ErrorRedirectInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //这里设置完之后，需要到common.js中设置jquery-ajax默认设置，详细查看$.ajaxSetup方法
        String type = request.getHeader("X-Requested-With") == null ? "" : request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(type)) {
            JSONObject outputMSg = new JSONObject();
            outputMSg.put("code", 500);
            outputMSg.put("conetent", "");
            outputMSg.put("msg", request.getAttribute("exception"));
            output(outputMSg, request, response);
            return false;
        } else {
            return true;
        }
    }

    public void output(Object json, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String header = request.getHeader("Origin");
        response.setContentType("text/html;charset=UTF-8;");
        response.setHeader("Access-Control-Allow-Origin", header);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE,token");
        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
        out.close();
    }
}
