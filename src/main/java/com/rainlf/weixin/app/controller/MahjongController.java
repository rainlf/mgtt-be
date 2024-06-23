package com.rainlf.weixin.app.controller;

import com.rainlf.weixin.app.dto.ApiResp;
import com.rainlf.weixin.app.dto.MahjongRoundInfoDto;
import com.rainlf.weixin.app.dto.MahjongRecordDto;
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

    @GetMapping("/palyer/ids")
    public ApiResp<List<Integer>> getPlayerIds() {
        log.info("getPlayerIds");
        List<Integer> playerIds = gameService.getMahjongPlayerIds();
        log.info("getPlayerIds, playerIds: {}", playerIds);
        return ApiResp.success(playerIds);
    }

    @PostMapping("/palyer")
    public ApiResp<Void> addPalyer(@RequestParam("id") Integer id) {
        log.info("addPalyer, id: {}", id);
        gameService.addMahjongPlayer(id);
        return ApiResp.success();
    }

    @DeleteMapping("/palyer")
    public ApiResp<Void> deletePlayer(@RequestParam("id") Integer id) {
        log.info("deletePlayer, id: {}", id);
        gameService.deleteMahjongPlayer(id);
        return ApiResp.success();
    }


    @PostMapping("/round")
    public ApiResp<Void> saveMahjongInfo(@RequestBody MahjongRoundInfoDto mahjongRoundInfoDto) {
        log.info("saveMahjongInfo, req: {}", mahjongRoundInfoDto);
        gameService.saveMahjongInfo(mahjongRoundInfoDto);
        return ApiResp.success();
    }

    @GetMapping("/records")
    public ApiResp<List<MahjongRecordDto>> getMahjongRecords(@RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber, @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        log.info("getMahjongRecords, pageNumber: {}, pageSize: {}", pageNumber, pageSize);
        return ApiResp.success(gameService.getMahjongRecords(pageNumber, pageSize));
    }

}
