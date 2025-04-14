package com.rainlf.mgttbe.infra.interceptor;

import com.rainlf.mgttbe.infra.db.dataobj.ApiMonitorLog;
import com.rainlf.mgttbe.infra.db.repository.ApiMonitorLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ApiMonitorInterceptor implements HandlerInterceptor {

    private final ApiMonitorLogRepository logRepository;

    public ApiMonitorInterceptor(ApiMonitorLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        BIzContext.initContext();
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            BIzContext.getContext().setEndTime(LocalDateTime.now());

            ApiMonitorLog log = new ApiMonitorLog();
            // 基础信息
            log.setUri(request.getRequestURI());
            log.setHttpMethod(request.getMethod());
            log.setStatusCode(response.getStatus());
            log.setCostTime((int) Duration.between(BIzContext.getContext().getStartTime(), BIzContext.getContext().getEndTime()).toMillis());
            log.setIpAddress(getClientIp(request));
            log.setCreateTime(LocalDateTime.now());
            log.setBizId(BIzContext.getContext().getBizId());

            // Controller方法信息
            if (handler instanceof HandlerMethod handlerMethod) {
                String className = handlerMethod.getBeanType().getSimpleName();
                String methodName = handlerMethod.getMethod().getName();
                log.setClassMethod(className + "#" + methodName);
            }

            // 请求参数
            log.setRequestParams(getRequestParams(request));

            // 异常处理
            if (ex != null) {
                log.setHasException(true);
                log.setExceptionStack(getStackTrace(ex));
            }

            // 保存日志
            logRepository.save(log);
        } finally {
            BIzContext.clearContext();
        }
    }

    private String getRequestParams(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        return params.entrySet().stream().map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue())).collect(Collectors.joining("&"));
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}