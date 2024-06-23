package com.rainlf.weixin.infra.db.model;

import com.rainlf.weixin.domain.consts.GameTypeEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
@Data
@Entity
@Table(name = "weixin_game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer gameType;
    private Integer recorderId;
    private Integer score;
    private String scoreExt;
    private String winCase;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;
}