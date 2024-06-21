package com.rainlf.weixin.app.controller;

import com.rainlf.weixin.app.dto.ApiResp;
import com.rainlf.weixin.app.dto.MahjongRecordDto;
import com.rainlf.weixin.app.dto.MahjongInfoDto;
import com.rainlf.weixin.domain.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author rain
 * @date 6/14/2024 8:14 PM
 */
@Slf4j
@RestController
@RequestMapping("/api/mahjong")
public class MahjongController {
    @Autowired
    private GameService gameService;

    @PostMapping("/record")
    public ApiResp<Void> saveMahjongInfo(@RequestBody MahjongInfoDto mahjongInfoDto) {
        log.info("saveMahjongInfo, req: {}", mahjongInfoDto);
        gameService.saveMahjongInfo(mahjongInfoDto);
        return ApiResp.success();
    }

    @GetMapping("/records")
    public ApiResp<List<MahjongRecordDto>> getMahjongRecords(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        log.info("getMahjongRecords, pageNumber: {}, pageSize: {}", pageNumber, pageSize);
        return ApiResp.success(gameService.getMahjongRecords(pageNumber, pageSize));
    }
}
