package com.rainlf.weixin.infra.db.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
@Data
@Table(name = "weixin_mahjong_record")
public class MahjongRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String roundId;
    private Integer recorderId;
    private String type;
    private Integer userId;
    private Integer score;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}