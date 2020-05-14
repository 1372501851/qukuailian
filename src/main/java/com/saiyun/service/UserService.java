package com.saiyun.service;

import com.saiyun.exception.TokenException;
import com.saiyun.mapper.ReceptionAccountMapper;
import com.saiyun.mapper.UserIdentityMapper;
import com.saiyun.mapper.UserMapper;
import com.saiyun.model.ReceptionAccount;
import com.saiyun.model.User;
import com.saiyun.model.UserIdentity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserIdentityMapper userIdentityMapper;
    @Autowired
    private ReceptionAccountMapper receptionAccountMapper;

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

    public User getUserByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        return userMapper.selectOneByUser(user);
    }

    public User getUserById(String userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    public void updateUser(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    public Map isAuth(String userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        Map<String, String> map = new HashMap<>();
        map.put("oneAuth",user.getOneAuth());
        if("0".equals(user.getOneAuth())){
            UserIdentity userIdentity = userIdentityMapper.selectByUsrId(userId);
            map.put("name",userIdentity.getRealname());
            map.put("certificateNumber",userIdentity.getCertificateNumber());
        }else{
            return map;
        }
        map.put("twoAuth",user.getTwoAuth());
        return map;
    }

    public User getUserByToken(String token) throws TokenException {
        User user = new User();
        user.setToken(token);
        User user1 = userMapper.selectOneByUser(user);
        if (user1 == null){
            throw  new TokenException("该token已过期或不存在");
        }
        return user1;
    }

    public void removeToken(String token) {
        userMapper.updateTokenNull(token);
    }

    public void addUser(User userByToken) {
         userMapper.insertSelective(userByToken);
    }

    public void addReceptionAccount(ReceptionAccount receptionAccount) {
        receptionAccountMapper.insertSelective(receptionAccount);
    }
}
