package com.rainlf.wxmp.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 4/20/2024 9:14 PM
 */
@Data
@Entity
public class MahjongUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String avatar;

    private Integer assets;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
