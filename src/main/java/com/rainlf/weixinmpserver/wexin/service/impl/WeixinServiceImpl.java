package com.rainlf.weixinmpserver.wexin.service.impl;

import com.rainlf.weixinmpserver.wexin.model.Code2SessionResp;
import com.rainlf.weixinmpserver.wexin.service.WeixinService;
import org.springframework.stereotype.Service;

/**
 * @author rain
 * @date 5/30/2024 11:53 AM
 */
@Service
public class WeixinServiceImpl implements WeixinService {
    @Override
    public Code2SessionResp code2Session(String code) {
        Code2SessionResp resp = new Code2SessionResp();
        resp.setOpenId("open-1");
        resp.setUnionId("union-1");
        resp.setSessionKey("key-1");
        return resp;
    }
}
