package com.rainlf.weixinmpserver.model;

import lombok.Data;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rain
 * @date 5/30/2024 4:36 PM
 */
@Data
public class GameInfo {
    private String gameId;
    private LocalDateTime gameTime;
    private String operator;
    private List<Item> itmeList;

    @Data
    public static class Item {
        private String username;
        private Integer assetChange;
    }
}
