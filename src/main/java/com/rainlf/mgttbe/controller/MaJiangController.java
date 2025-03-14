package com.rainlf.mgttbe.controller;

import com.rainlf.mgttbe.controller.dto.ApiResponse;
import com.rainlf.mgttbe.controller.dto.MaJiangGameLogDTO;
import com.rainlf.mgttbe.controller.dto.PlayersDTO;
import com.rainlf.mgttbe.controller.dto.SaveMaJiangGameRequest;
import com.rainlf.mgttbe.infra.util.JsonUtils;
import com.rainlf.mgttbe.service.MaJiangService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/majiang")
public class MaJiangController {

    @Autowired
    private MaJiangService majiangService;

    @GetMapping("/games")
    public ApiResponse<List<MaJiangGameLogDTO>> getMaJiangGames() {
        return ApiResponse.success(majiangService.getMaJiangGameLogs());
    }

    @GetMapping("/user/games")
    public ApiResponse<List<MaJiangGameLogDTO>> getMaJiangGamesByUser(@RequestParam("userId") Integer userId) {
        return ApiResponse.success(majiangService.getMaJiangGamesByUser(userId));
    }

    @PostMapping("/game")
    public ApiResponse<Integer> saveMaJiangGame(@RequestBody SaveMaJiangGameRequest request) {
        log.info("saveMaJiangGame, request: {}", JsonUtils.writeString(request));
        validSaveMaJiangGameRequest(request);
        return ApiResponse.success(majiangService.saveMaJiangGame(request));
    }

    @DeleteMapping("/game")
    public ApiResponse<Void> deleteMaJiangGame(@RequestParam("id") Integer id) {
        majiangService.deleteMaJiangGame(id);
        return ApiResponse.success();
    }

    @GetMapping("/game/players")
    public ApiResponse<PlayersDTO> getGamePlayers() {
        return ApiResponse.success(majiangService.getMaJiangGamePlayers());
    }


    private void validSaveMaJiangGameRequest(SaveMaJiangGameRequest request) {
        if (request.getPlayers() == null || request.getPlayers().size() != 4) {
            throw new RuntimeException("Invalid number of players");
        }

        if (request.getRecorderId() == null) {
            throw new RuntimeException("Invalid recorder id");
        }

        if (request.getWinners() == null || request.getWinners().isEmpty()) {
            throw new RuntimeException("Invalid winners, winners is empty");
        }

        if (request.getLosers() == null || request.getLosers().isEmpty()) {
            throw new RuntimeException("Invalid losers, losers is empty");
        }

        if (request.getWinners().stream().anyMatch(x -> x.getUserId() == null)) {
            throw new RuntimeException("Invalid winners, winners must contain at least one user");
        }

        if (request.getWinners().stream().anyMatch(x ->  x.getBasePoints() <= 0)) {
            throw new RuntimeException("Invalid winners, winners basepoint must be greater than 0");
        }

        List<Integer> players = request.getPlayers();
        List<Integer> winners = request.getWinners().stream().map(SaveMaJiangGameRequest.Winner::getUserId).toList();
        List<Integer> losers = request.getLosers();

        if (!Collections.disjoint(winners, losers)) {
            throw new RuntimeException("Invalid winners or losers, cannot have common user");
        }
        if (winners.stream().anyMatch(x -> !players.contains(x))) {
            throw new RuntimeException("Invalid winners, must be one of players");
        }
        if (losers.stream().anyMatch(y -> !players.contains(y))) {
            throw new RuntimeException("Invalid losers, must be one of players");
        }
    }
}


