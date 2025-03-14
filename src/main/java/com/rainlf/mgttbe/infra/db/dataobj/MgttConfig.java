package com.rainlf.mgttbe.infra.db.dataobj;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "mgtt_config")
public class MgttConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String key;
    private String value;
    @Column(insertable = false, updatable = false)
    private LocalDateTime createdTime;
    @Column(insertable = false, updatable = false)
    private LocalDateTime updatedTime;
}
