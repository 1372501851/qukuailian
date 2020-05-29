package com.saiyun.service;

import com.saiyun.mapper.UserWalletMapper;
import com.saiyun.model.UserWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserWalletService {
    @Autowired
    private UserWalletMapper userWalletMapper;
    //获取用户的指定币的钱包
    public UserWallet getUserWalletByUserIdAndCoinNo(String userId,String coinNo){
        UserWallet userWallet = new UserWallet();
        userWallet.setType(Integer.parseInt(coinNo));
        userWallet.setUserId(userId);
        return userWalletMapper.getUserWalletByCondition(userWallet);
    }

}
