package com.rainlf.weixin.domain.model;

/**
 * @author rain
 * @date 6/14/2024 8:30 PM
 */
public enum MahjongRecordType {
    GAME,
    SPORT,
    AWARD,
    ;

    public static MahjongRecordType fromString(String value) {
        for (MahjongRecordType type : MahjongRecordType.values()) {
            if (type.toString().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
