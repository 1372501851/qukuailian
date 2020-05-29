package com.saiyun.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public void addRedis(String key,String value){
        redisTemplate.opsForValue().set(key, value);//短信
    }
    public void addRedis(String key,String value,Integer expire){
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);//短信
    }
    public Object getRedis(String key){
        String cacheCode =(String) redisTemplate.opsForValue().get(key);
        return cacheCode;
    }
    /**
     * 是否过了发送间隔

     * @return
     */
    public boolean keyExtis(String key){
        if(redisTemplate.opsForValue().get(key)!=null){
            return true;
        }else {
            return false;
        }

    }
}
