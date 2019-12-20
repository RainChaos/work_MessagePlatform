package com.jiapeng.messageplatform.handler;

import com.jiapeng.messageplatform.utils.GetRootPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by HZL on 2019/5/20.
 */

@ControllerAdvice
public class MyControllerAdvice {
    @Resource
    GetRootPath getRootPath;
    private static final Logger logger = LoggerFactory.getLogger(MyControllerAdvice.class);

    /***
     * 404处理
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String notFountHandler(HttpServletRequest request, NoHandlerFoundException e) {
        return getRootPath.getPath(request) + "/error/404";
    }

    /**
     * 运行时异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.OK)
    public String processException(HttpServletRequest request, Model model, RuntimeException exception) {
        logger.info("自定义异常处理-RuntimeException");
        exception.printStackTrace();
        model.addAttribute("exception", exception.getMessage());
        return "forward:/error/error";
    }

    /**
     * Excepiton异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public String processException(HttpServletRequest request, Model model, Exception exception) {
        logger.info("自定义异常处理-Exception");
        exception.printStackTrace();
        model.addAttribute("exception", exception.getMessage());
        return "forward:/error/error";
    }

}
