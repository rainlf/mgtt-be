package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.domain.service.AuthService;
import com.rainlf.weixin.infra.db.model.User;
import com.rainlf.weixin.infra.db.model.UserAsset;
import com.rainlf.weixin.infra.db.repository.UserAssetRepository;
import com.rainlf.weixin.infra.db.repository.UserRepository;
import com.rainlf.weixin.infra.util.JwtUtils;
import com.rainlf.weixin.infra.wexin.model.WeixinSession;
import com.rainlf.weixin.infra.wexin.service.WeixinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UserRepository userRepository;
    @Autowired
    private UserAssetRepository userAssetRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String login(String code) {
        WeixinSession weixinSession = weixinService.code2Session(code);

        Optional<User> userOptional = userRepository.findByOpenId(weixinSession.getOpenId());

        if (userOptional.isEmpty()) {
            User user = new User();
            user.setOpenId(weixinSession.getOpenId());
            user.setUnionId(weixinSession.getUnionId());
            user.setSessionKey(weixinSession.getSessionKey());
            userRepository.save(user);

            UserAsset userAsset = new UserAsset();
            userAsset.setUserId(user.getId());
            userAssetRepository.save(userAsset);
        } else {
            User user = userOptional.get();
            user.setSessionKey(weixinSession.getSessionKey());
            userRepository.save(user);
        }

        return JwtUtils.generateToken(weixinSession.getOpenId());
    }
}
