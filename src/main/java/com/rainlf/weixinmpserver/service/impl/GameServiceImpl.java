package com.rainlf.weixinmpserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rainlf.weixinmpserver.infra.mapper.GameMapper;
import com.rainlf.weixinmpserver.infra.mapper.UserMapper;
import com.rainlf.weixinmpserver.model.db.Game;
import com.rainlf.weixinmpserver.model.GameInfo;
import com.rainlf.weixinmpserver.model.db.User;
import com.rainlf.weixinmpserver.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author rain
 * @date 5/30/2024 4:17 PM
 */
@Slf4j
@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GameMapper gameMapper;

    @Override
    @Transactional
    public void record(List<Game> games) {
        String gameId = UUID.randomUUID().toString().replace("-", "");
        games.forEach(x -> {
            x.setGameId(gameId);
            x.setType("TEST");
        });
        games.forEach(x -> gameMapper.insert(x));
        games.forEach(x -> userMapper.updateAssetChange(x.getUserId(), x.getAssetChange()));
    }

    @Override
    public List<GameInfo> getGameInfo() {
        List<Game> games = gameMapper.selectList(null);
        Map<String, List<Game>> gameMap = games.stream().collect(Collectors.groupingBy(Game::getGameId));

        List<User> users = userMapper.selectList(new QueryWrapper<User>().in("id", games.stream().map(Game::getUserId).collect(Collectors.toList())));
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getId, x -> x, (x, y) -> x));

        return gameMap.entrySet().stream()
                .map(x -> {
                    String gameId = x.getKey();
                    List<Game> gameItems = x.getValue();
                    GameInfo gameInfo = new GameInfo();
                    gameInfo.setGameId(gameId);
                    gameInfo.setGameTime(gameItems.get(0).getCreateTime());
                    gameInfo.setOperator(userMap.get(gameItems.get(0).getOperatorId()).getNickname());
                    gameInfo.setItmeList(gameItems.stream().map(y -> {
                        GameInfo.Item item = new GameInfo.Item();
                        item.setUsername(userMap.get(y.getUserId()).getNickname());
                        item.setAssetChange(y.getAssetChange());
                        return item;
                    }).collect(Collectors.toList()));
                    return gameInfo;
                })
                .collect(Collectors.toList());
    }

}
