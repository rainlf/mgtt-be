package com.rainlf.mgttbe.model;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class MaJiangGameInfo {
    private MaJiangGame game;
    private List<MaJiangGameItem> items;

    public MaJiangGameItem findUserGameItem(Integer userId) {
        for (MaJiangGameItem item : items) {
            if (Objects.equals(item.getUserId(), userId)) {
                return item;
            }
        }
        return null;
    }
}
