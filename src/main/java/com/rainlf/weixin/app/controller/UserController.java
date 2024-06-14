package com.rainlf.weixin.app.controller;


import com.rainlf.weixin.app.dto.ApiResp;
import com.rainlf.weixin.app.dto.UserInfo;
import com.rainlf.weixin.domain.service.UserService;
import com.rainlf.weixin.infra.db.model.User;
import com.rainlf.weixin.infra.sso.SsoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author rain
 * @date 5/30/2024 10:48 AM
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    public SsoService ssoService;
    @Autowired
    public UserService userService;

    @GetMapping("/current")
    public ApiResp<UserInfo> getCurrentUser() {
        User user = ssoService.getCurrentUser();
        log.info("getCurrentUser:{}", user);
        return ApiResp.success(userService.getUserInfo(user));
    }

    @GetMapping("/all")
    public ApiResp<List<UserInfo>> getAllUser() {
        return ApiResp.success(userService.getAllUserInfo());
    }
}
