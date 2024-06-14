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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        return createUserInfo(user, userAsset);
    }

    @Override
    public List<UserInfo> getAllUser() {
        List<User> userList = userMapper.findAll();
        List<UserAsset> userAssetList = userAssetMapper.findByUserIdIn(userList.stream().map(User::getId).toList());
        Map<Integer, UserAsset> userAssetMap = userAssetList.stream().collect(Collectors.toMap(UserAsset::getId, x -> x));

        return userList.stream()
                .map(user -> {
                    UserAsset userAsset = userAssetMap.get(user.getId());
                    return createUserInfo(user, userAsset);
                })
                .sorted(Comparator.comparing(UserInfo::getCopperCoin))
                .toList();
    }

    private UserInfo createUserInfo(User user, UserAsset userAsset) {
        UserInfo userInfo = new UserInfo();
        if (user != null) {
            userInfo.setNickname(user.getNickname());
            userInfo.setAvatar(user.getAvatar());
        }
        if (userAsset != null) {
            userInfo.setCopperCoin(userAsset.getCopperCoin());
            userInfo.setSilverCoin(userAsset.getSilverCoin());
            userInfo.setGoldCoin(userAsset.getGoldCoin());
        }
        return userInfo;
    }
}
