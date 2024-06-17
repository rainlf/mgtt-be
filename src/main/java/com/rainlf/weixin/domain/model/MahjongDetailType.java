package com.rainlf.weixin.domain.model;

/**
 * @author rain
 * @date 6/14/2024 8:30 PM
 */
public enum MahjongDetailType {
    GAME,
    SPORT,
    AWARD,
    ;

    public static MahjongDetailType fromString(String value) {
        for (MahjongDetailType type : MahjongDetailType.values()) {
            if (type.toString().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
