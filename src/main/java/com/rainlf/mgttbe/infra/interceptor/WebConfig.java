package com.rainlf.mgttbe.infra.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ApiMonitorInterceptor apiMonitorInterceptor;

    public WebConfig(ApiMonitorInterceptor apiMonitorInterceptor) {
        this.apiMonitorInterceptor = apiMonitorInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiMonitorInterceptor)
                .addPathPatterns("/**"); // 监控所有/api开头的接口
    }
}