package com.rainlf.weixin.domain.service;

/**
 * @author rain
 * @date 6/14/2024 11:23 AM
 */
public interface AuthService {

    String login(String code);

    String mockLogin();
}
