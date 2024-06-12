package com.rainlf.weixinmpserver.infra.wexin.service.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.rainlf.weixinmpserver.infra.wexin.model.Code2SessionResp;
import com.rainlf.weixinmpserver.infra.wexin.service.WeixinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

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
    public Code2SessionResp code2Session(String code) {
        log.info("code2Session, code: {}", code);
        Map<String, String> request = ImmutableMap.of(
                "appid", appId,
                "secret", secret,
                "js_code", code,
                "grant_type", "authorization_code"
        );
        Code2SessionResp resp = restTemplate.postForObject(code2Session, request, Code2SessionResp.class);
        log.info("code2Session, resp: {}", resp);

        if (resp == null) {
            throw new RuntimeException("code2Session error, resp is null");
        }

        if (resp.getErrcode() != 0) {
            throw new RuntimeException("code2Session error, resp: " + resp);
        }

        return resp;
    }
}
