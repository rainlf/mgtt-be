package com.rainlf.weixin.app.controller;


import com.rainlf.weixin.infra.db.model.User;
import com.rainlf.weixin.infra.sso.SsoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/current")
    public String current() {
        User user = ssoService.getCurrentUser();
        log.info("xx: {}", user);
        return "current";
    }
}
