package com.saiyun.core.security;

import com.saiyun.model.member.Member;
import com.saiyun.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author saiyun
 */
@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Member member = memberService.findByUsername(userName);
        if(member == null){
            throw new UsernameNotFoundException("userName" + userName +"not found");
        }

        return new CustomUser(member.getUid(),  member.getAccount(),    member.getPassword(),   member.getAuthorities());
    }
}
