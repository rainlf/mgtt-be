package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.UserInfo;
import com.rainlf.weixin.domain.service.UserService;
import com.rainlf.weixin.infra.db.mapper.UserAssetMapper;
import com.rainlf.weixin.infra.db.mapper.UserMapper;
import com.rainlf.weixin.infra.db.model.User;
import com.rainlf.weixin.infra.db.model.UserAsset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author rain
 * @date 6/14/2024 6:57 PM
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAssetMapper userAssetMapper;

    @Override
    public UserInfo getUserInfo(User user) {
        UserAsset userAsset = userAssetMapper.selectById(user.getId());
        if (userAsset == null) {
            throw new RuntimeException("user not found, id: " + user.getId());
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setCopperCoin(userAsset.getCopperCoin());
        userInfo.setSilverCoin(userAsset.getSilverCoin());
        userInfo.setGoldCoin(userAsset.getGoldCoin());
        return userInfo;
    }
}
