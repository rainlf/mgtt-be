package com.rainlf.weixin.infra.db.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
@Data
@Entity
@Table(name = "weixin_user")
@SQLRestriction("is_deleted = 0")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String openId;
    private String unionId;
    private String nickname;
    private String avatar;
    private String sessionKey;
    @Column(insertable = false)
    private boolean isAdmin;
    @Column(insertable = false)
    private boolean isDeleted;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;
}
