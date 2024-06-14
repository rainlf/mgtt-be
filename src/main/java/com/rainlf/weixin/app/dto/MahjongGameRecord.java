package com.rainlf.weixin.app.dto;

import com.rainlf.weixin.domain.model.MahjongRecordType;
import lombok.Data;

import java.util.List;

/**
 * @author rain
 * @date 6/14/2024 8:23 PM
 */
@Data
public class MahjongGameRecord {
    private Integer recorderId;
    private MahjongRecordType type;
    private List<Record> records;

    @Data
    public static class Record {
        private Integer userId;
        private Integer score;
    }
}
