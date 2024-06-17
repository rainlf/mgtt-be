package com.rainlf.weixin.app.dto;

import com.rainlf.weixin.domain.model.MahjongFanType;
import com.rainlf.weixin.domain.model.MahjongSiteType;
import com.rainlf.weixin.domain.model.MahjongWinType;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author rain
 * @date 6/17/2024 11:20 AM
 */
@Data
public class RoundInfoDto {
    private Integer roundId;
    private Integer recorderId;
    private List<Integer> winnerIds;
    private List<Integer> loserIds;
    private Integer baseFan;
    private MahjongWinType winType;
    private List<MahjongFanType> fanTypes;
    private Map<Integer, MahjongSiteType> siteMap;
}
