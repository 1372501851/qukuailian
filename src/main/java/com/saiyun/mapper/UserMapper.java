package com.saiyun.mapper;

import com.saiyun.core.CustomerMapper;
import com.saiyun.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserMapper {
    int deleteByPrimaryKey(String userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectOneByUser(User user);

    void updateTokenNull(String token);

    List<User> selectAll();

    User getOneByUserType(String sysUserStr);
}