package com.rainlf.weixin.app.controller;

import com.rainlf.weixin.domain.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam("code") String code) {
        log.info("login code:{}", code);
        return ResponseEntity.ok(authService.login(code));
    }
}
