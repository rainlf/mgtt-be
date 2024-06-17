package com.rainlf.weixin.app.dto;

import com.rainlf.weixin.domain.model.MahjongDetailType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author rain
 * @date 6/14/2024 8:23 PM
 */
@Data
public class MahjongRecordDto {
    private MahjongDetailType type;
    private Integer recorderId;
    private String recorderName;
    private String recorderAvatar;
    private List<RecordItem> recordItems;
    private LocalDateTime createTime;

    @Data
    public static class RecordItem {
        private Integer userId;
        private String userName;
        private String userAvatar;
        private Integer score;
    }

    public void checkValid() {
        if (Objects.equals(type, MahjongDetailType.GAME) && recordItems.stream().mapToInt(RecordItem::getScore).sum() != 0) {
            throw new RuntimeException("invalid record, score sum should be 0");
        }
    }
}
