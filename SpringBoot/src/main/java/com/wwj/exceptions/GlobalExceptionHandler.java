package com.wwj.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 * Created by sherry on 2017/4/9.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public String defaultErrHandler(HttpServletRequest request,Exception e) throws Exception{
        e.printStackTrace();
        request.setAttribute("exception", e);
        request.setAttribute("url", request.getRequestURL());
        return "err";
    }

}
