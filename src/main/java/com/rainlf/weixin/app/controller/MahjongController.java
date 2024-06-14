package com.rainlf.weixin.app.controller;

import com.rainlf.weixin.app.dto.ApiResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rain
 * @date 6/14/2024 8:14 PM
 */
@Slf4j
@RestController
@RequestMapping("/api/mahjong")
public class MahjongController {

    @PostMapping("/reocrd")
    public ApiResp<Void> saveRecord() {

        return ApiResp.success();
    }
}
