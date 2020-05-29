package com.saiyun.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

@Component
public class MessageSourceHandler {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String messageKey) {
        String message = messageSource.getMessage(messageKey, null, RequestContextUtils.getLocale(request));
        return message;
    }
}