package com.rainlf.weixinmpserver.controller;

import com.rainlf.weixinmpserver.controller.model.ApiResp;
import com.rainlf.weixinmpserver.model.db.Game;
import com.rainlf.weixinmpserver.model.GameInfo;
import com.rainlf.weixinmpserver.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author rain
 * @date 5/30/2024 4:15 PM
 */
@Slf4j
@RestController
@RequestMapping("game")
public class GameController {
    @Autowired
    private GameService gameService;

    @PostMapping("/record")
    public ApiResp<Void> record(@RequestBody List<Game> games) {
        log.info("game record, games: {}", games);
        gameService.record(games);
        return ApiResp.success();
    }

    @GetMapping("/info")
    public ApiResp<List<GameInfo>> getGameInfo() {
        return ApiResp.success(gameService.getGameInfo());
    }
}
