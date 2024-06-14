package com.rainlf.weixin.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rain
 * @date 5/30/2024 10:48 AM
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/current")
    public String current() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String xx = authentication.getName();
        log.info("xx: {}", xx);

        return "current";
    }
}
