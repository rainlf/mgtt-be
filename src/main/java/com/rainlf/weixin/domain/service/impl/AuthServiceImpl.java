package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.domain.service.AuthService;
import com.rainlf.weixin.infra.db.mapper.UserMapper;
import com.rainlf.weixin.infra.db.model.User;
import com.rainlf.weixin.infra.security.JwtTokenService;
import com.rainlf.weixin.infra.wexin.model.WeixinSession;
import com.rainlf.weixin.infra.wexin.service.WeixinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author rain
 * @date 6/14/2024 11:23 AM
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private WeixinService weixinService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtTokenService jwtTokenService;

    @Override
    public String login(String code) {
        WeixinSession weixinSession = weixinService.code2Session(code);

        Optional<User> userOptional = userMapper.findByOpenId(weixinSession.getOpenId());
        if (userOptional.isEmpty()) {
            User user = new User();
            user.setOpenId(weixinSession.getOpenId());
            user.setUnionId(weixinSession.getUnionId());
            user.setSessionKey(weixinSession.getSessionKey());
            userMapper.insert(user);
        } else {
            User user = userOptional.get();
            user.setSessionKey(weixinSession.getSessionKey());
            userMapper.updateById(user);
        }

        return jwtTokenService.generateToken(weixinSession.getOpenId());
    }
}
