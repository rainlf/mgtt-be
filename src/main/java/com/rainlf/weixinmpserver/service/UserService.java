package com.rainlf.weixinmpserver.service;

import com.rainlf.weixinmpserver.model.db.User;

/**
 * @author rain
 * @date 5/30/2024 10:54 AM
 */
public interface UserService {
    User login(String code);

    User update(User user);
}
