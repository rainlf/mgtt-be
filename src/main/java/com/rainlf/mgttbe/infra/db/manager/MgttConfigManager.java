package com.rainlf.mgttbe.infra.db.manager;

import com.rainlf.mgttbe.infra.db.repository.MgttConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MgttConfigManager {
    @Autowired
    private MgttConfigRepository mgttConfigRepository;

    public String getWxAppId() {
        return mgttConfigRepository.findByKey("wx.app.id").getValue();
    }

    public String getWxAppSecret() {
        return mgttConfigRepository.findByKey("wx.app.secret").getValue();
    }

    public String getWxLoginUrl() {
        return mgttConfigRepository.findByKey("wx.login.url").getValue();
    }
}
