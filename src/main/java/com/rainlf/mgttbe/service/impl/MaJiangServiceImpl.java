package com.rainlf.mgttbe.service.impl;

import com.rainlf.mgttbe.controller.dto.MaJiangGameLogDTO;
import com.rainlf.mgttbe.controller.dto.PlayersDTO;
import com.rainlf.mgttbe.controller.dto.SaveMaJiangGameRequest;
import com.rainlf.mgttbe.controller.dto.UserDTO;
import com.rainlf.mgttbe.infra.db.manager.MaJiangGameItemManager;
import com.rainlf.mgttbe.infra.db.manager.MaJiangGameManager;
import com.rainlf.mgttbe.infra.util.DateUtils;
import com.rainlf.mgttbe.model.*;
import com.rainlf.mgttbe.service.MaJiangService;
import com.rainlf.mgttbe.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MaJiangServiceImpl implements MaJiangService {
    @Autowired
    private MaJiangGameManager majiangGameManager;
    @Autowired
    private MaJiangGameItemManager majiangGameItemManager;
    @Autowired
    private UserService userService;
    @Autowired
    private Random random;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer saveMaJiangGame(SaveMaJiangGameRequest request) {
        MaJiangGame majiangGame = new MaJiangGame();
        majiangGame.setType(MaJiangGameType.fromCode(request.getGameType()));
        majiangGame.setPlayer1(request.getPlayers().get(0));
        majiangGame.setPlayer2(request.getPlayers().get(1));
        majiangGame.setPlayer3(request.getPlayers().get(2));
        majiangGame.setPlayer4(request.getPlayers().get(3));
        majiangGame = majiangGameManager.save(majiangGame);

        Integer gameId = majiangGame.getId();

        // winner
        List<MaJiangGameItem> winners = new ArrayList<>();
        for (SaveMaJiangGameRequest.Winner winner : request.getWinners()) {
            MaJiangGameItem maJiangGameItem = new MaJiangGameItem();
            maJiangGameItem.setGameId(gameId);
            maJiangGameItem.setUserId(winner.getUserId());
            maJiangGameItem.setType(MaJiangUserType.WINNER);
            maJiangGameItem.setBasePoint(winner.getBasePoints());
            maJiangGameItem.setWinTypes(winner.getWinTypes().stream().map(MaJiangWinType::fromName).toList());
            winners.add(maJiangGameItem);
        }

        // loser
        List<MaJiangGameItem> losers = new ArrayList<>();
        for (Integer loser : request.getLosers()) {
            MaJiangGameItem maJiangGameItem = new MaJiangGameItem();
            maJiangGameItem.setGameId(gameId);
            maJiangGameItem.setUserId(loser);
            maJiangGameItem.setType(MaJiangUserType.LOSER);
            losers.add(maJiangGameItem);
        }

        switch (majiangGame.getType()) {
            case PING_HU -> {
                if (winners.size() != 1 || losers.size() != 1) {
                    throw new RuntimeException("Ping HU losers and losers don't match");
                }
                MaJiangGameItem winner = winners.getFirst();
                winner.calculatePoints();
                losers.getFirst().setPoints(-winner.getPoints());
            }
            case ZI_MO -> {
                if (winners.size() != 1 || losers.size() != 3) {
                    throw new RuntimeException("ZI MO losers and losers don't match");
                }
                MaJiangGameItem winner = winners.getFirst();
                winner.calculatePoints();
                int points = winner.getPoints();
                winner.setPoints(points * 3);
                losers.forEach(x -> x.setPoints(-points));
            }
            case YI_PAO_SHUANG_XIANG -> {
                if (winners.size() != 2 || losers.size() != 1) {
                    throw new RuntimeException("YI PAO shuang xiang losers don't match");
                }
                int points = 0;
                for (MaJiangGameItem x : winners) {
                    x.calculatePoints();
                    points += x.getPoints();
                }
                losers.getFirst().setPoints(-points);
            }
            case YI_PAO_SAN_XIANG -> {
                if (winners.size() != 3 || losers.size() != 1) {
                    throw new RuntimeException("YI PAO san xiang losers don't match");
                }
                int points = 0;
                for (MaJiangGameItem x : winners) {
                    x.calculatePoints();
                    points += x.getPoints();
                }
                losers.getFirst().setPoints(-points);
            }
        }

        // recorder
        MaJiangGameItem recorder = new MaJiangGameItem();
        recorder.setGameId(gameId);
        recorder.setUserId(request.getRecorderId());
        recorder.setType(MaJiangUserType.RECORDER);
        // 生成一个 0 到 99 之间的随机整数, 1% 加 100, 99% 加 1
        int recorderPoints = (random.nextInt(100) < 1) ? 100 : 1;
        recorder.setPoints(recorderPoints);

        List<MaJiangGameItem> items = new ArrayList<>();
        items.addAll(winners);
        items.addAll(losers);
        items.add(recorder);
        majiangGameItemManager.save(items);

        // update user points with atom operation
        for (MaJiangGameItem item : items) {
            User user = userService.findUserById(item.getUserId());
            log.info("game save, id: {}, user: {}, points: {}", gameId, user.getUsername(), item.getPoints());
            userService.updateUserPoint(item.getUserId(), item.getPoints());
        }

        return gameId;
    }

    @Override
    public List<MaJiangGameLogDTO> getMaJiangGameLogs() {
        List<MaJiangGame> games = majiangGameManager.findLastGames(100);
        return buildMaJiangGameLogDTO(games);
    }

    @Override
    public List<MaJiangGameLogDTO> getMaJiangGamesByUser(Integer userId) {
        List<Integer> lastGameIds = majiangGameItemManager.findLastGameIdsByUserId(userId, 100);
        List<MaJiangGame> games = majiangGameManager.findByIdIn(lastGameIds);
        return buildMaJiangGameLogDTO(games);
    }

    private List<MaJiangGameLogDTO> buildMaJiangGameLogDTO(List<MaJiangGame> games) {
        List<MaJiangGameLogDTO> dtos = new ArrayList<>();
        for (MaJiangGame game : games) {
            MaJiangGameLogDTO dto = new MaJiangGameLogDTO();
            dto.setId(game.getId());
            if (Objects.equals(game.getType(), MaJiangGameType.ZI_MO)) {
                dto.setType(MaJiangGameType.ZI_MO.getName());
            } else {
                dto.setType(MaJiangGameType.PING_HU.getName());
            }

            dto.setPlayer1(UserDTO.fromUser(userService.findUserById(game.getPlayer1())));
            dto.setPlayer2(UserDTO.fromUser(userService.findUserById(game.getPlayer2())));
            dto.setPlayer3(UserDTO.fromUser(userService.findUserById(game.getPlayer3())));
            dto.setPlayer4(UserDTO.fromUser(userService.findUserById(game.getPlayer4())));
            dto.setCreatedTime(DateUtils.formatDateTime(game.getCreatedTime()));
            dto.setUpdatedTime(DateUtils.formatDateTime(game.getUpdatedTime()));

            List<MaJiangGameItem> gameItems = majiangGameItemManager.findByGameId(game.getId());
            List<MaJiangGameItem> winnerGameItems = gameItems.stream().filter(x -> Objects.equals(x.getType(), MaJiangUserType.WINNER)).toList();
            List<MaJiangGameItem> loserGameItems = gameItems.stream().filter(x -> Objects.equals(x.getType(), MaJiangUserType.LOSER)).toList();
            MaJiangGameItem recorderGameItems = gameItems.stream().filter(x -> Objects.equals(x.getType(), MaJiangUserType.RECORDER)).toList().getFirst();

            List<MaJiangGameLogDTO.Item> winnerItems = new ArrayList<>();
            for (MaJiangGameItem winnerGameItem : winnerGameItems) {
                MaJiangGameLogDTO.Item item = new MaJiangGameLogDTO.Item();
                item.setUser(UserDTO.fromUser(userService.findUserById(winnerGameItem.getUserId())));
                item.setPoints(winnerGameItem.getPoints());
                List<String> tags = new ArrayList<>();
                if (Objects.equals(game.getType(), MaJiangGameType.ZI_MO)) {
                    tags.add(game.getType().getName());
                } else {
                    tags.add(winnerGameItem.getType().getName());
                }
                if (winnerGameItem.getWinTypes() != null) {
                    tags.addAll(winnerGameItem.getWinTypes().stream().map(MaJiangWinType::getName).toList());
                }
                item.setTags(tags);
                winnerItems.add(item);
            }
            dto.setWinners(winnerItems);

            List<MaJiangGameLogDTO.Item> loserItems = new ArrayList<>();
            for (MaJiangGameItem loserGameItem : loserGameItems) {
                MaJiangGameLogDTO.Item item = new MaJiangGameLogDTO.Item();
                item.setUser(UserDTO.fromUser(userService.findUserById(loserGameItem.getUserId())));
                item.setPoints(loserGameItem.getPoints());
                List<String> tags = new ArrayList<>();
                if (Objects.equals(game.getType(), MaJiangGameType.YI_PAO_SHUANG_XIANG) || Objects.equals(game.getType(), MaJiangGameType.YI_PAO_SAN_XIANG)) {
                    tags.add(game.getType().getName());
                } else {
                    tags.add(loserGameItem.getType().getName());
                }
                item.setTags(tags);
                loserItems.add(item);
            }
            dto.setLosers(loserItems);

            MaJiangGameLogDTO.Item recorderItem = new MaJiangGameLogDTO.Item();
            recorderItem.setUser(UserDTO.fromUser(userService.findUserById(recorderGameItems.getUserId())));
            recorderItem.setPoints(recorderGameItems.getPoints());
            dto.setRecorder(recorderItem);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    @Transactional
    public void deleteMaJiangGame(Integer id) {
        MaJiangGame maJiangGame = majiangGameManager.findByIdWithLock(id);
        if (maJiangGame == null) {
            throw new RuntimeException("majianggame not exist, id: " + id);
        }

        List<MaJiangGameItem> maJiangGameItems = majiangGameItemManager.findByGameId(id);
        for (MaJiangGameItem maJiangGameItem : maJiangGameItems) {
            User user = userService.findUserById(maJiangGameItem.getUserId());
            log.info("game rollback, id: {}, user: {}, points: {}", id, user.getUsername(), -maJiangGameItem.getPoints());
            userService.updateUserPoint(maJiangGameItem.getUserId(), -maJiangGameItem.getPoints());
        }

        maJiangGame.setDeleted(true);
        majiangGameManager.save(maJiangGame);
    }

    @Override
    public PlayersDTO getMaJiangGamePlayers() {
        PlayersDTO playersDTO = new PlayersDTO();

        List<MaJiangGame> maJiangGames = majiangGameManager.findLastGames(1);
        if (maJiangGames.isEmpty()) {
            playersDTO.setCurrentPlayers(new ArrayList<>());
        } else {
            MaJiangGame maJiangGame = maJiangGames.getFirst();
            List<UserDTO> currentPlayers = new ArrayList<>();
            currentPlayers.add(UserDTO.fromUser(userService.findUserById(maJiangGame.getPlayer1())));
            currentPlayers.add(UserDTO.fromUser(userService.findUserById(maJiangGame.getPlayer2())));
            currentPlayers.add(UserDTO.fromUser(userService.findUserById(maJiangGame.getPlayer3())));
            currentPlayers.add(UserDTO.fromUser(userService.findUserById(maJiangGame.getPlayer4())));
            playersDTO.setCurrentPlayers(currentPlayers);
        }

        List<User> users = userService.findAllUsers();
        playersDTO.setAllPlayers(users.stream().map(UserDTO::fromUser).collect(Collectors.toList()));
        return playersDTO;
    }
}
