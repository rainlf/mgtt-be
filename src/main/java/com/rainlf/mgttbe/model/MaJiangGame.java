package com.rainlf.mgttbe.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaJiangGame {
    private Integer id;
    private MaJiangGameType type;
    private Integer player1;
    private Integer player2;
    private Integer player3;
    private Integer player4;
    private boolean deleted;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
