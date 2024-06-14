package com.rainlf.weixin.infra.security;

import com.rainlf.weixin.infra.db.mapper.UserMapper;
import com.rainlf.weixin.infra.db.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * @author rain
 * @date 6/14/2024 9:28 AM
 */
@Slf4j
@Component
public class WexinUserDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    private final String ROLE_ADMIN = "ROLE_ADMIN";
    private final String ROLE_USER = "ROLE_USER";

    @Override
    public UserDetails loadUserByUsername(String openId) throws UsernameNotFoundException {
        User user = userMapper.findByOpenId(openId)
                .orElseThrow(() -> new UsernameNotFoundException("User not exists by openId"));

        Collection<SimpleGrantedAuthority> authorities = user.isAdmin()
                ? List.of(new SimpleGrantedAuthority(ROLE_ADMIN))
                : List.of(new SimpleGrantedAuthority(ROLE_USER));

        return new org.springframework.security.core.userdetails.User(
                openId,
                null,
                authorities
        );
    }
}
