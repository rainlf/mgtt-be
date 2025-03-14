package com.rainlf.mgttbe.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum MaJiangWinType {
    WU_HUA_GUO("无花果", 1, 1),
    PENG_PENG_HU("碰碰胡", 2, 2),
    YI_TIAO_LONG("一条龙", 3, 2),
    HUN_YI_SE("混一色", 4, 2),
    QING_YI_SE("清一色", 5, 4),
    XIAO_QI_DUI("小七对", 6, 2),
    LONG_QI_DUI("龙七对", 7, 4),
    DA_DIAO_CHE("大吊车", 8, 2),
    MEN_QING("门前清", 9, 2),
    GANG_KAI("杠开花", 10, 2),
    ;

    private final String name;
    private final Integer code;
    private final Integer multi;

    MaJiangWinType(String name, Integer code, Integer multi) {
        this.name = name;
        this.code = code;
        this.multi = multi;
    }

    public static MaJiangWinType fromCode(Integer code) {
        for (MaJiangWinType majiangWinType : values()) {
            if (Objects.equals(majiangWinType.getCode(), code)) {
                return majiangWinType;
            }
        }
        throw new IllegalArgumentException("MaJiangUserType, no such code: " + code);
    }

    public static MaJiangWinType fromName(String name) {
        for (MaJiangWinType majiangWinType : values()) {
            if (Objects.equals(majiangWinType.getName(), name)) {
                return majiangWinType;
            }
        }
        throw new IllegalArgumentException("MaJiangUserType, no such name: " + name);
    }
}
