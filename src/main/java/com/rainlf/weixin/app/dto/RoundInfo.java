package com.rainlf.weixin.app.dto;

import java.util.List;

/**
 * @author rain
 * @date 6/17/2024 11:20 AM
 */
public class RoundInfo {
    private Integer recorderId;
    private List<Integer> winnerIds;
    private List<Integer> loserIds;
    private Integer baseFan;
    private WinType winType;
    private List<FanType> fanTypes;
    private List<SiteType> winnerSites;

    public enum SiteType {
        EAST,
        WEST,
        NORTH,
        SOUTH,
    }

    public enum WinType {
        COMMON_WIN,
        SELF_TOUCH_WIN,
        ONE_PAO_DOUBLE_WIN,
        ONE_PAO_TRIPLE_WIN,
    }

    public enum FanType {
        PENGPENG_FAN,
        SAME_COLOR_FAN,
        SEVEN_PAIR_FAN,
        DOOR_CLEAN_FAN,
        BIG_CRANE_FAN,
        FLOWER_OPEN_FAN,
    }
}
