package com.saiyun.mapper.member;

import com.saiyun.model.member.Member;
import com.saiyun.core.CustomerMapper;
import org.springframework.stereotype.Service;

/**
 * @author saiyun
 */
@Service
public interface MemberMapper extends CustomerMapper<Member> {
    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    Member selectByUsername(String username);
}
