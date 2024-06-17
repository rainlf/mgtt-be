package com.rainlf.weixin.app.controller;

import com.rainlf.weixin.app.dto.ApiResp;
import com.rainlf.weixin.domain.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author rain
 * @date 6/14/2024 11:21 AM
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ApiResp<String> login(@RequestParam("code") String code) {
        log.info("login code:{}", code);
        return ApiResp.success(authService.login(code));
    }

    @PostMapping("/mockLogin")
    public ApiResp<String> mockLogin() {
        return ApiResp.success(authService.mockLogin());
    }
}
