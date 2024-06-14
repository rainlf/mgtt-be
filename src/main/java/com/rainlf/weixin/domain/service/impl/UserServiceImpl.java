package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.UserInfo;
import com.rainlf.weixin.domain.service.UserService;
import com.rainlf.weixin.infra.db.model.User;
import com.rainlf.weixin.infra.db.model.UserAsset;
import com.rainlf.weixin.infra.db.repository.UserAssetRepository;
import com.rainlf.weixin.infra.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author rain
 * @date 6/14/2024 6:57 PM
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAssetRepository userAssetRepository;

    @Override
    public UserInfo getUserInfo(User user) {
        Optional<UserAsset> userAssetOptional = userAssetRepository.findByUserId(user.getId());
        if (userAssetOptional.isEmpty()) {
            throw new RuntimeException("user not found, id: " + user.getId());
        }

        return createUserInfo(user, userAssetOptional.get());
    }

    @Override
    public List<UserInfo> getAllUserInfo() {
        List<User> userList = userRepository.findAll();
        List<UserAsset> userAssetList = userAssetRepository.findByUserIdIn(userList.stream().map(User::getId).toList());
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
