package com.saiyun.controller.api;

import com.saiyun.exception.SMSFailException;
import com.saiyun.model.User;
import com.saiyun.service.RedisService;
import com.saiyun.service.RegisAndLoginService;
import com.saiyun.service.UserService;
import com.saiyun.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/app/")
public class ApiRegisAndLoginController {

    @Autowired
    private RegisAndLoginService regisAndLoginService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;
    //图形验证码key后缀
    private String imgCodeRedisSuffix = ":imgCode";
    private String smsCodeSuffix = ":interval";
    //图形验证码失效时间
    Integer imgCodeExpirationTime = 60*3;//3分钟
//    @RequestMapping(value = "emailRegis")
//    public ModelMap emailRegis(@RequestBody TreeMap<String,String> map){
//        String email = map.get("email");
//        String code = map.get("code");
//        String password = map.get("password");
//        if(!ValidataUtil.checkPwd(password)){
//            return ReturnUtil.error("请输入6-20位的密码");
//        }
//        //邮箱是否注册
//        if (regisAndLoginService.emailExist(email)){
//            return ReturnUtil.error("该邮箱已注册");
//        }
//        String regis = "1";
//        Object redis = redisService.getRedis(email + ":" + regis);
//
//    }
    /**
     * 手机注册
     */
    @RequestMapping(value = "phoneRegis",method = RequestMethod.POST)
    public ModelMap phoneRegis(@RequestBody TreeMap<String,String> map) throws Exception {
            String phone = map.get("phone");
            String code = map.get("code");
            String areaCode = "+"+map.get("area_code");//区号
            String password = map.get("password");
            if(!ValidataUtil.checkPwd(password)){
                return ReturnUtil.error("请输入6-20位的密码");
            }
            //是否已注册
            if(regisAndLoginService.exist(phone)){
                return ReturnUtil.error("该手机号已注册");
            }
            String regis = "1";
            String cacheCode = regisAndLoginService.getCacheCode(phone,regis,areaCode);
//            if (StringUtils.isEmpty(cacheCode)){
//                return ReturnUtil.error("验证码过期");
//            }
//            if (code.equals(cacheCode)){
                //注册成功
                regisAndLoginService.regis(phone,password,areaCode);
                return ReturnUtil.success("注册成功");
//            }else{
//                return ReturnUtil.error("验证码不正确");
//            }

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
            String areaCode = map.get("area_code");//区号
            if(StringCheckUtil.isEmpty(phone,scene,areaCode)){
                return ReturnUtil.error("请填写完整信息");
            }
            User user = userService.getUserByPhone(phone);
            if("1".equals(scene)){
                String code = map.get("code");
                String login = "2";
                String cacheCode = regisAndLoginService.getCacheCode(phone,login, areaCode);
                if (cacheCode == null || !cacheCode.equals(code)){
                    return ReturnUtil.error("验证码不正确");
                }
            }else if("2".equals(scene)){
                String password = map.get("password");
                if(password == null || user == null || !CommonUtils.calculateMD5(password).equals(user.getPassword())){
                    return  ReturnUtil.error("用户名或者密码不正确");
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
     * 图形验证码
     * @param map
     * @param req
     * @param resp
     */
    @PostMapping(value = "sendImgCode")
    public void getImgCode(@RequestBody TreeMap<String, String> map,HttpServletRequest req, HttpServletResponse resp){
        ServletOutputStream sos = null;
        try{
            String phone = map.get("phone");
            String areaCode = map.get("area_code");//区号
            Map<String, Object> returnMap = CodeUtil.generateCodeAndPic();
            String code = returnMap.get("code").toString();
            String imgRedisKey = areaCode+phone+imgCodeRedisSuffix;
            redisService.addRedis(imgRedisKey,code,imgCodeExpirationTime);
            // 禁止图像缓存。
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Cache-Control", "no-cache");
            resp.setDateHeader("Expires", -1);
            // 将图像输出到Servlet输出流中。
            resp.setContentType("image/jpeg");
            sos = resp.getOutputStream();
            ImageIO.write((RenderedImage) returnMap.get("codePic"), "jpeg", sos);
            sos.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(sos != null){
                    sos.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    /**
     * 验证手机号是否存在
     * @param map
     * @return
     */
    @PostMapping(value = "checkPhoneExit")
    public ModelMap checkPhoneExit(@RequestBody TreeMap<String, String> map){
        try{
            String phone = map.get("phone");
            boolean exist = regisAndLoginService.exist(phone);
            if (exist){
                return ReturnUtil.error("该手机号已存在");
            }else{
                return ReturnUtil.success("该手机号不存在");
            }
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
            String type = map.get("type");//1,短信 2，邮箱
            boolean flag = false;
            if("1".equals(type)){
                String phone = map.get("phone");
                String areaCode ="+"+ map.get("areaCode");//区号
                String key = areaCode+phone+":"+scene+":interval";
                if (redisService.keyExtis(key)){
                    return ReturnUtil.error("60s内只能发送一次");
                }
                flag = regisAndLoginService.sendMessage(phone, scene, areaCode);
            }else if("2".equals(type)){
                String email = map.get("email");
                String key = email+":"+scene+":interval";
                if (redisService.keyExtis(key)){
                    return ReturnUtil.error("60s内只能发送一次");
                }
                flag = regisAndLoginService.sendEmail(email,scene);
            }else{
                return ReturnUtil.error("参数不全");
            }
//            String imgCode = map.get("imgCode");//图形验证码
//            String imgRedisKey = areaCode+phone+imgCodeRedisSuffix;
//            String value = (String)redisService.getRedis(imgRedisKey);
//            if (StringUtils.isEmpty(value)){
//                return ReturnUtil.error("图形验证码过期");
//            }else{
//                if (!value.equals(imgCode)){
//                    return ReturnUtil.error("图形验证码不正确");
//                }
//            }
            if (flag){
                return ReturnUtil.success("验证码发送成功");
            }else {
                return ReturnUtil.error("验证码发送失败");
            }

        }catch (SMSFailException e){
            e.printStackTrace();
            return ReturnUtil.error("验证码发送失败");
        } catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }
    @PostMapping("checkCode")
    public ModelMap checkCode(@RequestBody TreeMap<String, String> map){
        try{
            String account = map.get("account");
            String code = map.get("code");
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }
}
