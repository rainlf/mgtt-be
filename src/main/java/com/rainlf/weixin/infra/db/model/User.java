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
@Table(name = "weixin_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String openId;
    private String unionId;
    private String nickname;
    private String avatar;
    private boolean admin;
    private String sessionKey;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
