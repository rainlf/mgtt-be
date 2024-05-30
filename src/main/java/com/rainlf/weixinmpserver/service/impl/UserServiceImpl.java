package com.rainlf.weixinmpserver.service.impl;

import com.rainlf.weixinmpserver.infra.mapper.UserMapper;
import com.rainlf.weixinmpserver.infra.wexin.model.Code2SessionResp;
import com.rainlf.weixinmpserver.infra.wexin.service.WeixinService;
import com.rainlf.weixinmpserver.model.User;
import com.rainlf.weixinmpserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rain
 * @date 5/30/2024 10:54 AM
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeixinService weixinService;

    @Override
    public User login(String code) {
        Code2SessionResp resp = weixinService.code2Session(code);

        User user = userMapper.selectByOpenId(resp.getOpenId());
        if (user == null) {
            user = new User();
            user.setOpenId(resp.getOpenId());
            user.setUnionId(resp.getUnionId());
            user.setSessionKey(resp.getSessionKey());
            userMapper.insert(user);
        } else {
            user.setSessionKey(resp.getSessionKey());
            userMapper.updateById(user);
        }
        return user;
    }

    @Override
    public User update(User user) {
        userMapper.updateById(user);
        return user;
    }
}
