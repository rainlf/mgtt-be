package com.rainlf.mgttbe.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum MaJiangUserType {
    WINNER(1, "赢家"),
    LOSER(2, "点炮"),
    RECORDER(3, "记账玩家"),
    ;

    private final Integer code;
    private final String name;

    MaJiangUserType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static MaJiangUserType fromCode(Integer code) {
        for (MaJiangUserType type : values()) {
            if (Objects.equals(type.code, code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("MaJiangUserType, no such code: " + code);
    }
}
