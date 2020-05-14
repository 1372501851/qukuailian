package com.saiyun.interceptor;

import com.saiyun.annotation.TokenRequired;
import com.saiyun.exception.TokenException;
import com.saiyun.mapper.UserMapper;
import com.saiyun.model.User;
import io.micrometer.core.instrument.util.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String jsons = httpServletRequest.getParameter("map");
        JSONObject j = JSONObject.fromObject(jsons);
        String token = j.getString("token");
        TokenRequired tokenRequired;
        if(object instanceof HandlerMethod){
            tokenRequired = ((HandlerMethod) object).getMethod().getDeclaringClass().getAnnotation(TokenRequired.class);
            if(tokenRequired == null){
                tokenRequired = ((HandlerMethod) object).getMethodAnnotation(TokenRequired.class);
            }
        }else{
            return true;
        }
        if(tokenRequired == null){
            return true;
        }
        if(StringUtils.isBlank(token)){
            throw new TokenException("token错误");
        }
        //查询token信息
        User u1 = new User();
        u1.setToken(token);
        User user = userMapper.selectOneByUser(u1);
        if(user == null ) {
            throw new TokenException("token不存在服务器");
        }
        return true;
    }

}
