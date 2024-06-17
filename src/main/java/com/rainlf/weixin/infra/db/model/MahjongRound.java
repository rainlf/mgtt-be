package com.rainlf.weixin.infra.db.model;

import com.rainlf.weixin.app.dto.RoundInfo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
@Data
@Entity
@Table(name = "weixin_mahjong_round")
@Where(clause = "deleted = 0")
public class MahjongRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer recorderId;
    private Integer baseFan;
    private String winType;
    private String fanTypes;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;
}