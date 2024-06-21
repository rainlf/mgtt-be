package com.rainlf.weixin.app.controller;

import com.rainlf.weixin.app.dto.ApiResp;
import com.rainlf.weixin.app.dto.MahjongRecordDto;
import com.rainlf.weixin.app.dto.SportInfoDto;
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
@RequestMapping("/api/sport")
public class SportController {
    @Autowired
    private GameService gameService;

    @PostMapping("/record")
    public ApiResp<Void> saveSportInfo(@RequestBody SportInfoDto sportInfoDto) {
        log.info("saveSportInfo, req: {}", sportInfoDto);
        gameService.saveSportInfo(sportInfoDto);
        return ApiResp.success();
    }

    @GetMapping("/records")
    public ApiResp<List<MahjongRecordDto>> getSportRecords(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        log.info("getSportRecords, pageNumber: {}, pageSize: {}", pageNumber, pageSize);
        return ApiResp.success(gameService.getMahjongRecords(pageNumber, pageSize));
    }
}
