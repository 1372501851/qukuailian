package com.saiyun.mapper;

import com.saiyun.model.UserWallet;
import org.springframework.stereotype.Service;

public interface UserWalletMapper {
    int deleteByPrimaryKey(Long userWalletId);

    int insert(UserWallet record);

    int insertSelective(UserWallet record);

    UserWallet selectByPrimaryKey(Long userWalletId);

    int updateByPrimaryKeySelective(UserWallet record);

    int updateByPrimaryKey(UserWallet record);

    UserWallet selectOneByUserId(String userId);
}