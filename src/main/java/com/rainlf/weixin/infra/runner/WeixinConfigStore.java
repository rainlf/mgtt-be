package com.rainlf.weixin.infra.runner;

import com.rainlf.weixin.infra.db.model.WeixinConfig;
import com.rainlf.weixin.infra.db.repository.WeixinConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rain
 * @date 6/23/2024 8:20 PM
 */
@Component
public class WeixinConfigStore implements ApplicationRunner {
    @Autowired
    private WeixinConfigRepository weixinConfigRepository;

    private final Map<String, String> configMap = new HashMap<>();

    public String getValue(String key) {
        return configMap.get(key);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<WeixinConfig> weixinConfigs = weixinConfigRepository.findAll();
        for (WeixinConfig weixinConfig : weixinConfigs) {
            configMap.put(weixinConfig.getKey(), weixinConfig.getValue());
        }
    }
}
