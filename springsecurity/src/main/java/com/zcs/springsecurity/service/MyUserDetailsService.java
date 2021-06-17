package com.zcs.springsecurity.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcs.springsecurity.entity.Users;
import com.zcs.springsecurity.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("admin");
//        return new User("zhao", new BCryptPasswordEncoder().encode("123456"),authorities);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);

        Users user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UsernameNotFoundException("Not found user");
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("admin");
        return new User(user.getUsername(), new BCryptPasswordEncoder().encode(user.getPassword()), authorities);
    }
}
