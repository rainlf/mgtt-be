package com.rainlf.mgttbe.service;

import com.rainlf.mgttbe.controller.dto.MaJiangGameLogDTO;
import com.rainlf.mgttbe.controller.dto.PlayersDTO;
import com.rainlf.mgttbe.controller.dto.SaveMaJiangGameRequest;

import java.util.List;

public interface MaJiangService {
    Integer saveMaJiangGame(SaveMaJiangGameRequest request);

    List<MaJiangGameLogDTO> getMaJiangGameLogs();

    List<MaJiangGameLogDTO> getMaJiangGamesByUser(Integer userId);

    void deleteMaJiangGame(Integer id);

    PlayersDTO getMaJiangGamePlayers();
}
