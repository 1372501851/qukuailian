package com.saiyun.service;

import com.saiyun.exception.SMSFailException;
import com.saiyun.mapper.UserMapper;
import com.saiyun.model.User;
import com.saiyun.model.console.AdminRole;
import com.saiyun.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RegisAndLoginService {
    /**图片存放地址*/
    @Value("${upload.config.hard-disk}")
    private String docBase;
    //短信验证码销毁时间
    Integer codeExpirationTime = 60*15;//15分钟
    //同个号码发送短信间隔
    Integer phoneSnedInterval = 60;//60s
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private UserMapper userMapper;
    public  String getCacheCode(String phone,String scene){
        //根据手机号取出验证码
        String cacheCode =(String) redisTemplate.opsForValue().get(phone+":"+scene);
       return cacheCode;
    }

    /**
     * 手机号是否存在
     * @param phone
     * @return
     */
    public boolean exist(String phone){
        User user = new User();
        user.setPhone(phone);
        User returnUser = userMapper.selectOneByUser(user);
        if(returnUser!=null){
            return true;
        }else{
            return false;
        }

    }
    public void regis(String phone,String password){
        User user = new User();
        user.setUserId(KeyId.nextId());
        user.setPassword(CommonUtils.calculateMD5(password));
        user.setCreatedate(CommonUtils.SimpleDateFormatTest(new Date()));
        String token = ValidataUtil.generateUUID();
        user.setToken(token);
        // 生成二维码
        user.setQrCodeUrl(QRCodeUtil.encode(user.getUserId(), "",user.getUserId(), docBase,"/qrcode/", true));
        userMapper.insertSelective(user);
    }


    /**
     * 发送短信
     * @param phone 电话号码
     * @param scene 1,注册 2，登录
     */
    public void  sendMessage(String phone,String scene) throws SMSFailException {
        String code =ValidataUtil.getRandomSix();
        System.out.println(code);
        //短信发送
        if(false){
            System.out.println("发送短信成功");
        }else{
            throw new SMSFailException("短信发送失败");
        }
        //存进redis
        redisTemplate.opsForValue().set(phone+":"+scene, code, codeExpirationTime, TimeUnit.SECONDS);//短信
        redisTemplate.opsForValue().set(phone+":"+scene+":interval",phone,phoneSnedInterval, TimeUnit.SECONDS);
    }

    /**
     * 是否过了短信发送间隔
     * @param phone 电话号码
     * @param scene
     * @return
     */
    public boolean phoneExtis(String phone,String scene){
        if(redisTemplate.opsForValue().get(phone+":"+scene+":interval")==null){
            return true;
        }else {
            return false;
        }

    }


    public String login(User user) {
        String token = ValidataUtil.generateUUID();
        user.setToken(token);
        userMapper.updateByPrimaryKeySelective(user);
        return token;
    }
}
