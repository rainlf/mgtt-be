package com.rainlf.weixin.domain.service;

import com.rainlf.weixin.app.dto.UserInfoDto;
import com.rainlf.weixin.infra.db.model.User;

import java.util.List;

/**
 * @author rain
 * @date 5/30/2024 10:54 AM
 */
public interface UserService {

    UserInfoDto getUserInfo(User user);

    List<UserInfoDto> getAllUserInfo();


    UserInfoDto updateCurrentUser(User user, String nickname, String avatar);
}
