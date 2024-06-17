package com.rainlf.weixin.domain.model;

/**
 * @author rain
 * @date 6/14/2024 8:30 PM
 */
public enum RecordType {
    GAME,
    SPORT,
    AWARD,
    ;

    public static RecordType fromString(String value) {
        for (RecordType type : RecordType.values()) {
            if (type.toString().equals(value)) {
                return type;
            }
        }
        return null;
    }
}
