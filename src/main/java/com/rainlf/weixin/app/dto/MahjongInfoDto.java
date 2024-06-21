package com.rainlf.weixin.app.dto;

import com.rainlf.weixin.domain.consts.MahjongScoreExtEnum;
import com.rainlf.weixin.domain.consts.MahjongWinerCaseEnum;
import lombok.Data;

import java.util.List;

/**
 * @author rain
 * @date 6/17/2024 11:20 AM
 */
@Data
public class MahjongInfoDto {
    private Integer gameId;
    private Integer recorderId;
    private List<Integer> winnerIds;
    private List<Integer> loserIds;
    private Integer baseScore;
    private MahjongWinerCaseEnum winerCase;
    private List<MahjongScoreExtEnum> scoreExtList;
}
