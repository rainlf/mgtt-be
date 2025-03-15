package com.rainlf.mgttbe.controller;


import com.rainlf.mgttbe.controller.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthController {

    @GetMapping("")
    public ApiResponse<String> ok() {
        log.info("ok");
        return ApiResponse.success("ok");
    }
}
