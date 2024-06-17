package com.rainlf.weixin.domain.service;

import com.rainlf.weixin.app.dto.MahjongRecord;
import com.rainlf.weixin.app.dto.RoundInfo;

import java.util.List;

/**
 * @author rain
 * @date 6/14/2024 8:32 PM
 */
public interface MahjongService {
    void saveRecord(RoundInfo roundInfo);

    List<MahjongRecord> getRecords(Integer pageNumber, Integer pageSize);
}
