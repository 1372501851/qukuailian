package com.saiyun.mapper;

import com.saiyun.model.UserIdentity;
import org.springframework.stereotype.Service;

@Service
public interface UserIdentityMapper {
    int deleteByPrimaryKey(String userIdentityId);

    int insert(UserIdentity record);

    int insertSelective(UserIdentity record);

    UserIdentity selectByPrimaryKey(String userIdentityId);

    int updateByPrimaryKeySelective(UserIdentity record);

    int updateByPrimaryKey(UserIdentity record);

    UserIdentity selectOneByUserId(String userId);
}