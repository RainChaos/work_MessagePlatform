package com.jiapeng.messageplatform.interceptor;
import com.jiapeng.messageplatform.service.DBChangeService;
import com.jiapeng.messageplatform.utils.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by HZL on 2019/11/8.
 */

public class DBChangeInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private DBChangeService dbChangeService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String scCode = request.getSession().getAttribute("scCode")==null?"":request.getSession().getAttribute("scCode").toString();
        if(!"".equals(scCode)) {
            dbChangeService.changeDb(scCode);
        }
        return super.preHandle(request, response, handler);
    }
}
