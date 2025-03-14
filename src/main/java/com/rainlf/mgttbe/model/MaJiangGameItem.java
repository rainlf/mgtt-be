package com.rainlf.mgttbe.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Data
public class MaJiangGameItem {
    private Integer id;
    private Integer gameId;
    private Integer userId;
    private MaJiangUserType type;
    private Integer basePoint;
    private List<MaJiangWinType> winTypes;
    private Integer multi;
    private Integer points;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public void calculatePoints() {
        log.info("calculate points, basePoint: {}, winTypes: {}", basePoint, winTypes);
        multi = 1;
        if (winTypes != null) {
            for (MaJiangWinType winType : winTypes) {
                multi *= winType.getMulti();
            }
        }
        points = multi * basePoint;
        log.info("calculate points, basePoint: {}, multi: {}, points: {}", basePoint, multi, points);
    }
}
