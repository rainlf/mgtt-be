package com.rainlf.wxmp.controller;

import com.alibaba.fastjson.JSON;
import com.rainlf.wxmp.dao.MahjongUser;
import com.rainlf.wxmp.model.ApiResponse;
import com.rainlf.wxmp.model.UserAssetsChange;
import com.rainlf.wxmp.service.MahjongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author rain
 * @date 4/20/2024 9:59 PM
 */
@Slf4j
@RestController
@RequestMapping("/mahjong")
public class MahjongController {
    @Autowired
    private MahjongService mahjongService;

    @GetMapping("/test")
    public ApiResponse<String> test() {
        log.info("test ok");
        return ApiResponse.success("ok");
    }

    @GetMapping("/logs")
    public ApiResponse<List<String>> getLogs() {
        log.info("get logs");
        return ApiResponse.success(mahjongService.findAllLog());
    }

    @PostMapping("/log")
    public ApiResponse<Void> addLog(@RequestBody String message) {
        log.info("add log: {}", log);
        mahjongService.insertLog(message);
        return ApiResponse.success();
    }

    @GetMapping("/users")
    public ApiResponse<List<MahjongUser>> getUsers() {
        log.info("get users");
        return ApiResponse.success(mahjongService.findAllUser());
    }

    @PostMapping("/user")
    public ApiResponse<Void> updateUser(@RequestBody UserAssetsChange userAssetsChange) {
        log.info("update user: {}", JSON.toJSONString(userAssetsChange));
        mahjongService.updateUser(userAssetsChange);
        return ApiResponse.success();
    }
}
