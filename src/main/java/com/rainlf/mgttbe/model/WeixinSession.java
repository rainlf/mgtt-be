package com.rainlf.mgttbe.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class WeixinSession {
    @JsonProperty("openid")
    private String openId;
    @JsonProperty("unionid")
    private String unionId;
    @JsonProperty("session_key")
    private String sessionKey;
    @JsonProperty("errcode")
    private Integer errcode;
    @JsonProperty("errmsg")
    private String errmsg;

    public boolean invalid() {
        return !StringUtils.hasText(openId)
                || !StringUtils.hasText(sessionKey);
    }
}
