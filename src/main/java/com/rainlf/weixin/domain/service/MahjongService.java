package com.rainlf.weixin.domain.service;

import com.rainlf.weixin.app.dto.MahjongRecordDto;
import com.rainlf.weixin.app.dto.RoundInfoDto;

import java.util.List;

/**
 * @author rain
 * @date 6/14/2024 8:32 PM
 */
public interface MahjongService {
    void saveRecord(RoundInfoDto roundInfoDto);

    List<MahjongRecordDto> getRecords(Integer pageNumber, Integer pageSize);
}
