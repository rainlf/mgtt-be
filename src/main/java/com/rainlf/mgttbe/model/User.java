package com.rainlf.mgttbe.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class User {
    private Integer id;
    private String username;
    private byte[] avatar;
    private String openId;
    private String sessionKey;
    private Integer points;
    private List<String> lastTags;
    private boolean deleted;
    private LocalDateTime lastLoginTime;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
