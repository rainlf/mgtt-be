package com.rainlf.weixinmpserver.service.impl;

import com.rainlf.weixinmpserver.infra.mapper.GameMapper;
import com.rainlf.weixinmpserver.infra.mapper.UserMapper;
import com.rainlf.weixinmpserver.model.Game;
import com.rainlf.weixinmpserver.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

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
        games.forEach(x -> x.setGameId(gameId));
        games.forEach(x -> gameMapper.insert(x));
        games.forEach(x-> userMapper.updateAssetChange(x.getUserId(),x.getAssetChange()));
    }

}
