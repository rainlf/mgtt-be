package com.rainlf.weixinmpserver.controller;

import com.rainlf.weixinmpserver.model.ApiResp;
import com.rainlf.weixinmpserver.model.User;
import com.rainlf.weixinmpserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rain
 * @date 5/30/2024 10:48 AM
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResp<User> login(@RequestParam("code") String code) {
        log.info("user login code: {}", code);
        return ApiResp.success(userService.login(code));
    }
}
