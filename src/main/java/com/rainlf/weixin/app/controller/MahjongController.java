package com.rainlf.weixin.app.controller;

import com.rainlf.weixin.app.dto.ApiResp;
import com.rainlf.weixin.app.dto.MahjongRecordDto;
import com.rainlf.weixin.app.dto.RoundInfoDto;
import com.rainlf.weixin.domain.service.MahjongService;
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
    private MahjongService mahjongService;

    @PostMapping("/reocrd")
    public ApiResp<Void> saveRecord(@RequestBody RoundInfoDto roundInfoDto) {
        log.info("saveRecord, roundInfo: {}", roundInfoDto);
        mahjongService.saveRecord(roundInfoDto);
        return ApiResp.success();
    }

    @GetMapping("/records")
    public ApiResp<List<MahjongRecordDto>> getRecords(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        log.info("getRecords, pageNumber: {}, pageSize: {}", pageNumber, pageSize);
        return ApiResp.success(mahjongService.getRecords(pageNumber, pageSize));
    }
}
