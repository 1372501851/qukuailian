package com.saiyun.exception;

import com.saiyun.config.MessageSourceHandler;
import com.saiyun.util.ReturnUtil;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MyException.class)
    public ModelMap handleMyException(MyException e){
        e.printStackTrace();
        return ReturnUtil.error(e.getMessage());
    }
    // 引入国际化处理类
    @Autowired
    private MessageSourceHandler messageSourceHandler;
    @ExceptionHandler(TokenException.class)
    public ModelMap handleTokenException(TokenException e){
        e.printStackTrace();
        return ReturnUtil.error(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ModelMap handleException(Exception e){
        e.printStackTrace();
        //发送通知（邮件等）
        return ReturnUtil.error("服务器错误，请联系管理员");
    }
    // 具体异常处理类
    private String handleException(Exception e, String code, Object body) {
        String msgKey = e.getMessage();
        String msg = msgKey;
        try {
            msg = messageSourceHandler.getMessage(msgKey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (StringUtils.isEmpty(msg)) {
            if (StringUtils.isEmpty(msgKey)) {
//                msg = messageSourceHandler.getMessage(ErrorTypeEnum.INTERNAL_SERVER_ERROR.getMessage());
            } else {
                msg = msgKey;
            }
        }
        return msg;
    }
}
