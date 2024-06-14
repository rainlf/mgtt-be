package com.rainlf.weixin.infra.wexin.service;

import com.rainlf.weixin.infra.wexin.model.WeixinSession;

/**
 * @author rain
 * @date 5/30/2024 10:56 AM
 */
public interface WeixinService {

    WeixinSession code2Session(String code);

}
