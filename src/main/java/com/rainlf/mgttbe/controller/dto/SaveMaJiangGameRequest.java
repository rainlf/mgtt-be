package com.rainlf.mgttbe.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaveMaJiangGameRequest {
    private Integer gameType;
    private List<Integer> players;
    private Integer recorderId;
    private List<Winner> winners;
    private List<Integer> losers;

    @Data
    public static class Winner {
        private Integer userId;
        private Integer basePoints;
        private List<String> winTypes;
    }
}
