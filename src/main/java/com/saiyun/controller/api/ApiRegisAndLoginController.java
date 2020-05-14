package com.saiyun.controller.api;

import com.saiyun.exception.SMSFailException;
import com.saiyun.model.User;
import com.saiyun.service.RegisAndLoginService;
import com.saiyun.service.UserService;
import com.saiyun.util.CommonUtils;
import com.saiyun.util.ReturnUtil;
import com.saiyun.util.StringCheckUtil;
import com.saiyun.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/app/")
public class ApiRegisAndLoginController {
    @Autowired
    private RegisAndLoginService regisAndLoginService;
    @Autowired
    private UserService userService;

    /**
     * 注册
     * @param phone
     * @return
     */
    @RequestMapping(value = "regis",method = RequestMethod.POST)
    public ModelMap regis(@RequestBody TreeMap<String,String> map){
        try{
            String phone = map.get("phone");
            String code = map.get("code");
            String password = map.get("password");
            if (StringCheckUtil.isEmpty(phone,password,code)){
                return ReturnUtil.error("请填写完整信息");
            }
            //是否已注册
            if(regisAndLoginService.exist(phone)){
                return ReturnUtil.error("该手机号已注册");
            }
            String regis = "1";
            String cacheCode = regisAndLoginService.getCacheCode(phone,regis);
            if (StringUtils.isEmpty(cacheCode)){
                return ReturnUtil.error("验证码过期");
            }
            if (code.equals(cacheCode)){
                //注册成功
                regisAndLoginService.regis(phone,password);
                return ReturnUtil.success("注册成功");
            }else{
                return ReturnUtil.error("验证码不正确");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

    /**
     * 登录
     * @param map  scene 1，验证码登录  2，密码登录
     * @return
     */
    @RequestMapping(value = "login",method = RequestMethod.POST)
    public ModelMap login(@RequestBody TreeMap<String, String> map){
        try{
            String phone = map.get("phone");
            String scene = map.get("scene");

            if(StringCheckUtil.isEmpty(phone)){
                return ReturnUtil.error("请输入手机号");
            }
            User user = userService.getUserByPhone(phone);
            if("1".equals(scene)){
                String code = map.get("code");
                String login = "2";
                String cacheCode = regisAndLoginService.getCacheCode(phone,login);
                if (cacheCode == null || !cacheCode.equals(code)){
                    return ReturnUtil.error("验证码不正确");
                }
            }else if("2".equals(scene)){
                String password = map.get("password");
                if(password == null || user == null || !CommonUtils.calculateMD5(password).equals(user.getPassword())){
                    return  ReturnUtil.error("密码不正确");
                }
            }else{
                return ReturnUtil.error("信息不全");
            }
            String token = regisAndLoginService.login(user);
            return ReturnUtil.success("登录成功",token);
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

    /**
     * 短信验证码发送

     */
    @RequestMapping(value = "getverificationcode",method = RequestMethod.POST)
    public ModelMap sendMessage(@RequestBody TreeMap<String, String> map){
        try{
            //1,注册发送短信，2，登录发送短信 3,修改密码 4,找回密码
            String scene = map.get("scene");
            String phone = map.get("phone");
            if(StringCheckUtil.isEmpty(phone,scene)){
                return ReturnUtil.error("请填写完整信息");
            }
            //60s内只能发送一次
            if(regisAndLoginService.phoneExtis(phone,scene)){
                return ReturnUtil.error("60s内只能发送一次");
            }
            regisAndLoginService.sendMessage(phone,scene);

            return ReturnUtil.success("验证码发送成功");
        }catch (SMSFailException e){
            e.printStackTrace();
            return ReturnUtil.error("短信发送失败");
        } catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }



}
