package com.rainlf.weixin.domain.service;

import com.rainlf.weixin.app.dto.MahjongRecordDto;
import com.rainlf.weixin.app.dto.MahjongInfoDto;
import com.rainlf.weixin.app.dto.SportInfoDto;

import java.util.List;

/**
 * @author rain
 * @date 6/14/2024 8:32 PM
 */
public interface GameService {
    void saveMahjongInfo(MahjongInfoDto mahjongInfoDto);

    void saveSportInfo(SportInfoDto sportInfoDto);

    List<MahjongRecordDto> getMahjongRecords(Integer pageNumber, Integer pageSize);
}
