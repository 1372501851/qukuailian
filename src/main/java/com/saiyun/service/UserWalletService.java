package com.saiyun.service;

import com.saiyun.mapper.UserWalletMapper;
import com.saiyun.model.User;
import com.saiyun.model.UserWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserWalletService {
    @Autowired
    private UserWalletMapper userWalletMapper;

    /**
     * 获取用户钱包信息
     * @param userid
     * @return
     */
    public UserWallet getUserWalletByUserId(String userid){
        UserWallet userWallet = userWalletMapper.selectOneByUserId(userid);
        return userWallet;
    }

    /**
     * 判断传入b数量是否比数据库里的少，如果数据库大于传入，返回true,否则返回false
     * @param bSum 币数量
     * @param bType 币类型 1,btc 2.usdt
     * @return
     */
    public boolean enough(String bSum, String bType, User user) throws Exception {
        UserWallet userWallet = userWalletMapper.selectOneByUserId(user.getUserId());//用户钱包数据
        BigDecimal bBalance = null ;
        if("1".equals(bType)){
            bBalance = userWallet.getBtcBalance();//可用btc
        }else if("2".equals(bType)){
            bBalance = userWallet.getUsdtBalance();//可用usdt
        }else {
            throw new Exception("参数异常,传入了一个不存在的参数");
        }
        if(bBalance.compareTo(new BigDecimal(bSum))>=0){
            return true;
        }else{
            return false;
        }

    }

}
