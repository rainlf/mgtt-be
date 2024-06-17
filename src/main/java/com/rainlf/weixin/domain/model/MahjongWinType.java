package com.rainlf.weixin.domain.model;

import lombok.Getter;

/**
 * @author rain
 * @date 6/17/2024 2:35 PM
 */
@Getter
public enum MahjongWinType {
    COMMON_WIN(1, 1),
    SELF_TOUCH_WIN(1, 3),
    ONE_PAO_DOUBLE_WIN(2, 1),
    ONE_PAO_TRIPLE_WIN(3, 1),
    ;

    private final int winnerNumber;
    private final int loserNumber;

    MahjongWinType(int winnerNumber, int loserNumber) {
        this.winnerNumber = winnerNumber;
        this.loserNumber = loserNumber;
    }
}
