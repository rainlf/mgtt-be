package com.rainlf.weixin.infra.db.model;

import com.rainlf.weixin.domain.consts.GameDetailTypeEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
@Data
@Entity
@Table(name = "weixin_game_detail")
public class GameDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer gameId;
    private Integer userId;
    private Integer type;
    private Integer score;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;
}