package com.rainlf.weixinmpserver.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
@Data
@TableName(value = "game")
public class Game {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String gameId;
    private Integer userId;
    private Integer assetChange;
    private String type;
    private Integer operatorId;
    private Date createTime;
    private Date updateTime;
}