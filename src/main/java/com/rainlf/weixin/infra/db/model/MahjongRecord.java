package com.rainlf.weixin.infra.db.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
@Data
@Entity
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
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;
}