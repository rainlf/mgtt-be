package com.rainlf.weixin.app.controller;

import com.rainlf.weixin.app.dto.ApiResp;
import com.rainlf.weixin.app.dto.MahjongRecordInfo;
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
    public ApiResp<Void> saveRecord(@RequestBody MahjongRecordInfo mahjongRecordInfo) {
        log.info("saveRecord, mahjongGameRecord: {}", mahjongRecordInfo);
        mahjongRecordInfo.checkValid();
        mahjongService.saveRecord(mahjongRecordInfo);
        return ApiResp.success();
    }

    @GetMapping("/records")
    public ApiResp<List<MahjongRecordInfo>> getRecords() {
        log.info("getRecords");
        return ApiResp.success(mahjongService.getRecords());
    }
}
