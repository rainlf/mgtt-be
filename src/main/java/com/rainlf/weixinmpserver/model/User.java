package com.rainlf.weixinmpserver.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String openId;
    private String unionId;
    private String nickname;
    private String avatar;
    private Integer asset;
    private Boolean admin;
    private String sessionKey;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
