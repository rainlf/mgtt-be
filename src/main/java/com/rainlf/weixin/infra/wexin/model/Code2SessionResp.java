package com.rainlf.weixin.infra.wexin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author rain
 * @date 5/30/2024 10:57 AM
 */
@Data
public class Code2SessionResp {
    @JsonProperty("openid")
    private String openId;
    @JsonProperty("session_key")
    private String sessionKey;
    @JsonProperty("unionid")
    private String unionId;
    private Integer errcode;
    private String errmsg;
}
