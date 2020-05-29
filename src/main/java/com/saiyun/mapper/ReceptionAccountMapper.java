package com.saiyun.mapper;

import com.saiyun.model.ReceptionAccount;

import java.util.List;

public interface ReceptionAccountMapper {
    int deleteByPrimaryKey(String accountId);

    int insert(ReceptionAccount record);

    int insertSelective(ReceptionAccount record);

    ReceptionAccount selectByPrimaryKey(String accountId);

    int updateByPrimaryKeySelective(ReceptionAccount record);

    int updateByPrimaryKey(ReceptionAccount record);

    List<ReceptionAccount> getByUserId(String userId);
}