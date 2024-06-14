package com.rainlf.weixin.domain.service;

import com.rainlf.weixin.app.dto.MahjongGameRecord;

/**
 * @author rain
 * @date 6/14/2024 8:32 PM
 */
public interface MahjongService {
    void saveRecord(MahjongGameRecord mahjongGameRecord);
}
