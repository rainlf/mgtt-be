package com.rainlf.weixin.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> handleException(Exception exception) {
        log.error("biz api error", exception);
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }
}

