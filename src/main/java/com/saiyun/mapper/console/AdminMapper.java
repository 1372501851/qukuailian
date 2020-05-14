package com.saiyun.mapper.console;

import com.saiyun.model.console.Admin;
import com.saiyun.core.CustomerMapper;
import org.springframework.stereotype.Service;

/**
 * @author saiyun
 */
@Service
public interface AdminMapper extends CustomerMapper<Admin> {
    /**
     * 根据用户名获取用户
     * @param userName
     * @return
     */
    Admin selectByUsername(String userName);

    /**
     * 根据ID删除
     * @param id
     */
    void deleteById(String id);
}
