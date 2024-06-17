package com.rainlf.weixin.domain.service;

import com.rainlf.weixin.app.dto.MahjongRecordDto;
import com.rainlf.weixin.app.dto.RoundInfoDto;
import com.rainlf.weixin.app.dto.SportInfoDto;

import java.util.List;

/**
 * @author rain
 * @date 6/14/2024 8:32 PM
 */
public interface MahjongService {
    void saveRoundInfo(RoundInfoDto roundInfoDto);

    void saveSportInfo(SportInfoDto sportInfoDto);

    List<MahjongRecordDto> getRecords(Integer pageNumber, Integer pageSize);
}
