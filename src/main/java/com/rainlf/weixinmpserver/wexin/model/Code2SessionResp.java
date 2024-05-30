package com.rainlf.weixinmpserver.wexin.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author rain
 * @date 5/30/2024 10:57 AM
 */
@Data
public class Code2SessionResp {
    @SerializedName("openid")
    private String openId;
    @SerializedName("session_key")
    private String sessionKey;
    @SerializedName("unionid")
    private String unionId;
    private Integer errcode;
    private String errmsg;
}
