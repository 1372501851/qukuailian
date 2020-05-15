package com.saiyun.controller.api;

import com.saiyun.annotation.TokenRequired;
import com.saiyun.model.ReceptionAccount;
import com.saiyun.model.User;
import com.saiyun.service.RegisAndLoginService;
import com.saiyun.service.UserService;
import com.saiyun.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.tree.Tree;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * 用户操作相关
 */
@RestController
@RequestMapping("/app/user/")
public class ApiUserController {
    @Autowired
    private RegisAndLoginService regisAndLoginService;
    @Autowired
    private UserService userService;
    /**
     * 修改密码
     * @param map
     * @return
     */
    @TokenRequired
    @RequestMapping(value = "changePassword",method = RequestMethod.POST)
    public ModelMap changePassword(@RequestBody TreeMap<String, Object> map){
        try{
            String token = (String) map.get("token");
            String code = (String) map.get("code");
            String password = (String) map.get("password");
            User user = userService.getUserByToken(token);
            String changePasswordScene = "3";
            String cacheCode = regisAndLoginService.getCacheCode(user.getPhone(), changePasswordScene);
            if(cacheCode == null || !cacheCode.equals(code)){
                return ReturnUtil.error("验证码不正确");
            }
            if (!ValidataUtil.checkPwd(password)){
                return ReturnUtil.error("请输入6-20位的密码");
            }
            if(StringUtils.isEmpty(password)){
                return ReturnUtil.error("请输入密码");
            }
            user.setPassword(CommonUtils.calculateMD5(password));
            userService.updateUser(user);
            return ReturnUtil.success("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

    /**
     * 找回密码
     * @param map
     * @return
     */
    @RequestMapping(value = "retrievePassword",method = RequestMethod.POST)
    public ModelMap retrievePassword(@RequestBody TreeMap<String,Object> map){
        try{
            String phone = (String) map.get("phone");
            String code = (String) map.get("code");
            String password = (String) map.get("password");
            String retrievePasswordScene = "4";
            String cacheCode = regisAndLoginService.getCacheCode(phone, retrievePasswordScene);

            if(cacheCode == null || !cacheCode.equals(code)){
                return ReturnUtil.error("验证码不正确");
            }
            if(StringUtils.isEmpty(password)){
                return ReturnUtil.error("请输入密码");
            }
            User user = userService.getUserByPhone(phone);
            user.setPassword(CommonUtils.calculateMD5(password));
            userService.updateUser(user);
            return ReturnUtil.success("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

    /**
     * 跟新昵称
     * @param map
     * @return
     */
    @TokenRequired
    @RequestMapping(value = "updateUser",method = RequestMethod.POST)
    public ModelMap updateUser(@RequestBody TreeMap<String,Object> map){
        try{
            String token = map.get("token").toString();
            String nickname = map.get("nickname").toString();
            User user = userService.getUserByToken(token);
            if(StringUtils.isEmpty(nickname)){
                return ReturnUtil.error("请输入昵称");
            }
            user.setNickname(nickname);
            userService.updateUser(user);
            return ReturnUtil.success("修改成功");
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

    /**
     * 用户注销
     * @param map
     * @return
     */
    @TokenRequired
    public ModelMap logout(@RequestBody TreeMap<String,Object> map){
        try{
            String token = map.get("token").toString();
                userService.removeToken(token);
                return ReturnUtil.success("退出成功");
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

    /**
     * 用户认证查询
     * @param map
     * @return
     */
    @RequestMapping(value = "isAuth",method = RequestMethod.POST)
    public ModelMap oneAuth(@RequestBody TreeMap<String,Object> map){
        try{
            String token = map.get("token").toString();
            User userByToken = userService.getUserByToken(token);
            Map returnMap = userService.isAuth(userByToken.getUserId());
            return ReturnUtil.success("请求成功",returnMap);
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

    /**
     * 更新头像
     * @param map
     * @return
     */
    @PostMapping("updateIcon")
    @TokenRequired
    public ModelMap updateIcon(@RequestBody TreeMap<String,Object> map){
        try{
            String token = (String) map.get("token");
            User userByToken = userService.getUserByToken(token);
            String iconUrl = (String)map.get("iconUrl");
            if (StringUtils.isEmpty(iconUrl)){
                return ReturnUtil.error("头像地址不能为空");
            }
            userByToken.setIconUrl(iconUrl);
            userService.addUser(userByToken);
            return ReturnUtil.success("操作成功");
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

    /**
     * 添加收款账号
     * @param map
     * @return
     */
    @PostMapping("addReceptionAccount")
    @TokenRequired
    public ModelMap addReceptionAccount(@RequestBody TreeMap<String,Object> map){
        try{
            String token = (String) map.get("token");
            User userByToken = userService.getUserByToken(token);
            String type = (String)map.get("type");//1,微信 2，支付宝 3，银行卡
            ReceptionAccount receptionAccount = new ReceptionAccount();
            if("3".equals(type)){
                String bankname = (String)map.get("bankname");
                String bankAddress = (String)map.get("bankAddress");
                if(StringUtils.isEmpty(bankname)){
                    return ReturnUtil.error("参数不全");
                }
                receptionAccount.setBankname(bankname);
                receptionAccount.setBankAddress(bankAddress);
            }
            receptionAccount.setAccountId(KeyId.nextId());
            receptionAccount.setUserId(userByToken.getUserId());
            receptionAccount.setType(type);
            String name = (String)map.get("name");
            String account = (String)map.get("account");
            String imgUrl = (String)map.get("imgUrl");
            String timeString = ValidataUtil.dateFormat(new Date());
            receptionAccount.setName(name);
            receptionAccount.setAccount(account);
            receptionAccount.setImgUrl(imgUrl);
            receptionAccount.setCreatedate(timeString);
            userService.addReceptionAccount(receptionAccount);
            return ReturnUtil.success("添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return ReturnUtil.error("后台错误，请联系管理员");
        }
    }

}
