package com.rainlf.mgttbe.infra.interceptor;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BIzContext {
    private static final ThreadLocal<BIzContext> contextHolder = new ThreadLocal<>();

    private BIzContext() {}

    private String bizId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static void initContext () {
        BIzContext bIzContext = new BIzContext();
        bIzContext.setBizId(UUID.randomUUID().toString());
        bIzContext.setStartTime(LocalDateTime.now());
        contextHolder.set(bIzContext);
    }

    public static BIzContext getContext() {
        BIzContext bIzContext =  contextHolder.get();
        if (bIzContext == null) {
            throw new RuntimeException("BIzContext not set");
        }
        return bIzContext;
    }

    public static void clearContext() {
        contextHolder.remove();
    }


}
