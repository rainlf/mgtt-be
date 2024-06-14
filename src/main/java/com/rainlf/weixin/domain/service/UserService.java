package com.rainlf.weixin.domain.service;

import com.rainlf.weixin.app.dto.UserInfo;
import com.rainlf.weixin.infra.db.model.User;

/**
 * @author rain
 * @date 5/30/2024 10:54 AM
 */
public interface UserService {

    UserInfo getUserInfo(User user);
}
