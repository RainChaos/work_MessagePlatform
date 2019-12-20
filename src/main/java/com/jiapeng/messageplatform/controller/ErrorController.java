package com.jiapeng.messageplatform.controller;

import com.jiapeng.messageplatform.annotation.LoginLimit;
import com.jiapeng.messageplatform.utils.GetRootPath;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by HZL on 2019/7/1.
 */
@LoginLimit(limit = false)
@Controller
@RequestMapping("/error")
public class ErrorController {
    @Resource
    GetRootPath getRootPath;

    @RequestMapping("/error")
    public String errorPage(HttpServletRequest request) {
        return getRootPath.getPath(request) + "/error/error";
    }

}
