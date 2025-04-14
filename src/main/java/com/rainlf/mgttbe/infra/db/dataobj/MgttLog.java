package com.rainlf.mgttbe.infra.db.dataobj;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mgtt_log")
public class MgttLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String level;
    private String thread;
    private String message;
    private String stackTrace;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updatedTime;
    @Column(name = "biz_id")
    private String bizId;
}
