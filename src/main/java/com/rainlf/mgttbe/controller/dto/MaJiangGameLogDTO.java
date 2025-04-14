package com.rainlf.mgttbe.controller.dto;

import lombok.Data;

import java.util.List;

@Data
public class MaJiangGameLogDTO {
    private Integer id;
    private String type;
    private UserDTO player1;
    private UserDTO player2;
    private UserDTO player3;
    private UserDTO player4;
    private String createdTime;
    private String updatedTime;
    private List<Item> winners;
    private List<Item> losers;
    private Item recorder;
    private boolean forOnePlayer;
    private boolean playerWin;


    @Data
    public static class Item {
        private UserDTO user;
        private Integer points;
        private List<String> tags;
    }
}
