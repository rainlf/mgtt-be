package com.rainlf.weixinmpserver.controller;

import com.rainlf.weixinmpserver.controller.model.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author rain
 * @date 5/30/2024 3:20 PM
 */
@Slf4j
@ControllerAdvice
public class AdviceController {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResp<String> handleException(Exception exception) {
        log.error("controller handleException", exception);
        return ApiResp.fail(exception.getMessage());
    }
}
