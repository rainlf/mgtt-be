package com.rainlf.mgttbe.controller;


import com.rainlf.mgttbe.controller.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class AdviceController {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResponse<String> customException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ApiResponse.failure(exception.getMessage());
    }
}


