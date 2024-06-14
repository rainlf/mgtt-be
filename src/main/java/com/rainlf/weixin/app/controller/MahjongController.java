package com.rainlf.weixin.app.controller;

import com.rainlf.weixin.app.dto.ApiResp;
import com.rainlf.weixin.app.dto.MahjongGameRecord;
import com.rainlf.weixin.domain.service.MahjongService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Autowired
    private MahjongService mahjongService;

    @PostMapping("/reocrd")
    public ApiResp<Void> saveRecord(@RequestBody MahjongGameRecord mahjongGameRecord) {
        log.info("saveRecord, mahjongGameRecord: {}", mahjongGameRecord);
        mahjongGameRecord.checkValid();
        mahjongService.saveRecord(mahjongGameRecord);
        return ApiResp.success();
    }

}
