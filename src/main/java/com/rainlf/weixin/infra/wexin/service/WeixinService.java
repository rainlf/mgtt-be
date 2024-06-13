package com.rainlf.weixin.infra.wexin.service;

import com.rainlf.weixin.infra.wexin.model.Code2SessionResp;

/**
 * @author rain
 * @date 5/30/2024 10:56 AM
 */
public interface WeixinService {

    Code2SessionResp code2Session(String code);

}
