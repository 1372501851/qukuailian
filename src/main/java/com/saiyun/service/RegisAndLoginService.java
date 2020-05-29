package com.saiyun.service;

import com.saiyun.exception.SMSFailException;
import com.saiyun.mapper.CoinMapper;
import com.saiyun.mapper.UserMapper;
import com.saiyun.mapper.UserWalletMapper;
import com.saiyun.model.Coin;
import com.saiyun.model.User;
import com.saiyun.model.UserWallet;
import com.saiyun.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
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
    @Autowired
    private UserWalletMapper userWalletMapper;
    @Autowired
    private CoinMapper coinMapper;
    public  String getCacheCode(String phone, String scene, String areaCode){
        //根据手机号取出验证码
        String cacheCode =(String) redisTemplate.opsForValue().get(areaCode+phone+":"+scene);
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
    @Transactional
    public void regis(String phone, String password, String areaCode) throws Exception {
        User user = new User();
        user.setUserId(KeyId.nextId());
        user.setAreaCode(areaCode);
        user.setPhone(phone);
        user.setPassword(CommonUtils.calculateMD5(password));
        user.setCreatedate(CommonUtils.SimpleDateFormatTest(new Date()));
        String token = ValidataUtil.generateUUID();
        user.setToken(token);
        userMapper.insertSelective(user);
        //todo:创建用户钱包
        this.createUserWallet(user);

    }

    public void createUserWallet(User user) throws Exception {
        //创建用户钱包
        List<Coin> coins = coinMapper.queryAllCoinList();
        for (Coin coin:coins) {
            UserWallet userWallet = new UserWallet();
            userWallet.setId(KeyId.nextId());
            userWallet.setUserId(user.getUserId());
            userWallet.setType(Integer.parseInt(coin.getCoinNo()+""));
            userWallet.setState(1);
            userWallet.setDate(ValidataUtil.dateFormat(new Date()));
            BigDecimal bigDecimal = new BigDecimal("0");
            userWallet.setBalance(bigDecimal);
            userWallet.setUnbalance(bigDecimal);
            userWalletMapper.insertUserWallet(userWallet);
        }
    }


    /**
     * 发送短信
     * @param phone 电话号码
     * @param scene 1,注册 2，登录
     * @param areaCode
     */
    public boolean  sendMessage(String phone, String scene, String areaCode) throws SMSFailException, IOException {
        String code =ValidataUtil.getRandomSix();
        System.out.println(code);
        //短信发送
        String flag = YunPianSmsUtils.sendSmsByTpl(areaCode, phone, code);
        if("success".equals(flag)){
            //存进redis
            String key = areaCode+phone+":"+scene;
            redisTemplate.opsForValue().set(key, code, codeExpirationTime, TimeUnit.SECONDS);//短信
            redisTemplate.opsForValue().set(key+":interval",phone,phoneSnedInterval, TimeUnit.SECONDS);//间隔
            return true;
        }else{
            return false;
        }

    }

    /**
     * 是否过了短信发送间隔
     * @param phone 电话号码
     * @param scene
     * @param areaCode
     * @return
     */
    public boolean phoneExtis(String phone, String scene, String areaCode){
        if(redisTemplate.opsForValue().get(areaCode+phone+":"+scene+":interval")==null){
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

    public boolean sendEmail(String email, String scene) {
        String code =ValidataUtil.getRandomSix();
        // Teleporturl Teleportcode
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<table cellpadding='0' cellspacing='0' style='border: 1px solid #cdcdcd; width: 640px; margin:auto;font-size: 12px; color: #1E2731; line-height: 20px;'>");
        stringBuffer.append("<tbody><tr>");
        stringBuffer.append("<td colspan='3' align='center' style='background-color:#454c6d; height: 55px; padding: 30px 0'><a href='Teleporturl' target='_blank' rel='noopener'><img src='javascript:;'></a></td>");
        stringBuffer.append("</tr><tr style='height: 30px;'></tr><tr><td width='20'></td><td style='line-height: 40px'>您好：<br>");
        stringBuffer.append("【Teleport】注册验证码：<b>Teleportcode</b>，您正在尝试【注册】，该验证码将于15分钟后失效。<br>");
        stringBuffer.append("验证码仅用于Huobi官网，如果非本人操作，请不要在任何地方输入该验证码并立即修改登录密码！ <br>为防止非法诈骗，请勿将帐号、密码、验证码在除官网外任何其他第三方网站输入。<br>");
        stringBuffer.append("</td><td width='20'></td></tr><tr style='height: 20px;'></tr><tr><td width='20'></td><td><br>Teleport团队<br>");
        stringBuffer.append("<a href='Teleporturl' rel='noopener' target='_blank'>Teleporturl</a><br></td><td width='20'></td></tr>");
        stringBuffer.append("<tr style='height: 50px;'></tr></tbody></table>");
        String body = stringBuffer.toString();
        String teleporturl = body.replaceAll("Teleporturl", "https://www.baidu.com/");
        String teleportcode = teleporturl.replaceAll("Teleportcode", code);
        boolean b = EmailUtil.sendMail(email, teleportcode, "【Teleport】注册成功");
        if(b){
            String key = email+":"+scene;
            redisTemplate.opsForValue().set(key, code, codeExpirationTime, TimeUnit.SECONDS);//短信
            redisTemplate.opsForValue().set(key+":interval",code,phoneSnedInterval, TimeUnit.SECONDS);//间隔
            return true;
        }else{
            return false;
        }

    }

    public boolean emailExist(String email) {
        User user = new User();
        user.setEmail(email);
        User user1 = userMapper.selectOneByUser(user);
        if (user1 != null){
            return true;
        }
        return false;
    }
}
