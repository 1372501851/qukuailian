package com.saiyun.exception;

import com.saiyun.util.ReturnUtil;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(TokenException.class)
    public ModelMap handleTokenException(TokenException e){
        e.printStackTrace();
        return ReturnUtil.error("token异常，请重新登录");
    }
    @ExceptionHandler(Exception.class)
    public ModelMap handleException(Exception e){
        //发送通知（邮件等）
        return ReturnUtil.error("服务器错误，请联系管理员");
    }
}
