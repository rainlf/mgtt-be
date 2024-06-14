package com.rainlf.weixin.infra.wexin.service.impl;

import com.google.common.collect.ImmutableMap;
import com.rainlf.weixin.infra.wexin.model.WeixinSession;
import com.rainlf.weixin.infra.wexin.service.WeixinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author rain
 * @date 5/30/2024 11:53 AM
 */
@Slf4j
@Service
public class WeixinServiceImpl implements WeixinService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${weixin.appid}")
    private String appId;

    @Value("${weixin.secret}")
    private String secret;

    @Value("${weixin.api.code2Session}")
    private String code2Session;

    @Override
    public WeixinSession code2Session(String code) {
        log.info("code2Session, code: {}", code);
        Map<String, String> request = ImmutableMap.of(
                "appid", appId,
                "secret", secret,
                "js_code", code,
                "grant_type", "authorization_code"
        );
        WeixinSession resp = restTemplate.postForObject(code2Session, request, WeixinSession.class);
        log.info("code2Session, resp: {}", resp);

        if (resp == null) {
            throw new RuntimeException("code2Session error, resp is null");
        }

        if (resp.getErrcode() != 0) {
            throw new RuntimeException("code2Session error, resp: " + resp);
        }

        if (!resp.valid()) {
            throw new RuntimeException("code2Session error, resp is invalid: " + resp);
        }

        return resp;
    }
}
