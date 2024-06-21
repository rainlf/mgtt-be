package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.MahjongRecordDto;
import com.rainlf.weixin.app.dto.MahjongInfoDto;
import com.rainlf.weixin.app.dto.SportInfoDto;
import com.rainlf.weixin.domain.consts.GameDetailTypeEnum;
import com.rainlf.weixin.domain.consts.GameDetailType;
import com.rainlf.weixin.domain.consts.MahjongSiteType;
import com.rainlf.weixin.domain.service.GameService;
import com.rainlf.weixin.infra.db.model.Game;
import com.rainlf.weixin.infra.db.model.GameDetail;
import com.rainlf.weixin.infra.db.model.User;
import com.rainlf.weixin.infra.db.model.UserAsset;
import com.rainlf.weixin.infra.db.repository.GameDetailRepository;
import com.rainlf.weixin.infra.db.repository.GameRepository;
import com.rainlf.weixin.infra.db.repository.UserAssetRepository;
import com.rainlf.weixin.infra.db.repository.UserRepository;
import com.rainlf.weixin.infra.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author rain
 * @date 6/14/2024 8:32 PM
 */
@Slf4j
@Service
public class GameServiceImpl implements GameService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAssetRepository userAssetRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameDetailRepository gameDetailRepository;

    @Value("${recorder.award.random.max}")
    private int randomAwardMax;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMahjongInfo(MahjongInfoDto mahjongInfoDto) {
        Game game = new Game();
        game.setRecorderId(mahjongInfoDto.getRecorderId());
        game.setScore(mahjongInfoDto.getBaseScore());
        game.setScoreExt(JsonUtils.toJson(mahjongInfoDto.getScoreExtList()));
        game.setWinerCase(mahjongInfoDto.getWinerCase().toString());
        game = gameRepository.save(game);
        mahjongInfoDto.setGameId(game.getId());

        int fan = calculateTotalScore(mahjongInfoDto.getBaseScore(), mahjongInfoDto.getScoreExtList().size());
        log.info("total fan: {}", fan);

        List<User> users = new ArrayList<>();
        Map<Integer, Integer> socreMap = new HashMap<>();
        List<User> winners = userRepository.findAllById(mahjongInfoDto.getWinnerIds());
        List<User> losers = userRepository.findAllById(mahjongInfoDto.getLoserIds());

        int winerNumber = mahjongInfoDto.getWinerCase().getWinnerNumber();
        int loserNumber = mahjongInfoDto.getWinerCase().getLoserNumber();
        if (winners.size() != winerNumber || losers.size() != loserNumber) {
            throw new RuntimeException("not expect scenario");
        }

        users.addAll(winners);
        users.addAll(losers);
        winners.forEach(winner -> socreMap.put(winner.getId(), fan * loserNumber));
        losers.forEach(loser -> socreMap.put(loser.getId(), -fan * winerNumber));
        log.info("socre info: {}", socreMap);

        saveGameDetail(users, socreMap, mahjongInfoDto.getGameId());
        log.info("save game detail success");

        int award = new Random().nextInt(randomAwardMax) + 1;
        log.info("recorder info, userId: {}, score: {}", mahjongInfoDto.getRecorderId(), award);
        saveRecorderDetail(game.getId(), mahjongInfoDto.getRecorderId(), award, GameDetailTypeEnum.MAHJONG_AWARD);
        log.info("save recorder detail success");
    }


    @Override
    public void saveSportInfo(SportInfoDto sportInfoDto) {
        log.info("sport info, userId: {}, score: {}", sportInfoDto.getSporterId(), sportInfoDto.getSportNumber());
        saveRecorderDetail(null, sportInfoDto.getSporterId(), sportInfoDto.getSportNumber(), GameDetailTypeEnum.REPAYMENT_SPORT);
        log.info("save sporter detail success");
    }

    @Override
    public List<MahjongRecordDto> getMahjongRecords(Integer pageNumber, Integer pageSize) {
        return null;
    }

    private void saveRecorderDetail(Integer gameId, Integer userId, Integer score, GameDetailTypeEnum type) {
        User user = userRepository.findById(userId).orElseThrow();
        UserAsset userAsset = userAssetRepository.findByUserId(userId).orElseThrow();

        userAsset.setCopperCoin(userAsset.getCopperCoin() + score);
        userAssetRepository.save(userAsset);

        GameDetail detail = new GameDetail();
        detail.setType(type);
        detail.setUserId(userId);
        detail.setScore(score);
        gameDetailRepository.save(detail);
    }

    private void saveGameDetail(List<User> users, Map<Integer, Integer> socreMap, Integer gameId) {
        // find asset
        List<UserAsset> userAssets = userAssetRepository.findByUserIdIn(users.stream().map(User::getId).toList());
        Map<Integer, UserAsset> userAssetMap = userAssets.stream().collect(Collectors.toMap(UserAsset::getUserId, x -> x));

        // change asset and insert game details
        List<GameDetail> gameDetails = new ArrayList<>();
        users.forEach(user -> {
            Integer userId = user.getId();
            // change user asset
            UserAsset userAsset = userAssetMap.get(userId);
            userAsset.setCopperCoin(userAsset.getCopperCoin() + socreMap.get(userId));

            // insert game details
            GameDetail detail = new GameDetail();
            detail.setGameId(gameId);
            detail.setType(GameDetailTypeEnum.MAHJONG_GAME);
            detail.setUserId(userId);
            detail.setScore(socreMap.get(userId));
            gameDetails.add(detail);
        });
        userAssetRepository.saveAll(userAssets);
        gameDetailRepository.saveAll(gameDetails);
    }

    private int calculateTotalScore(int score, int doubleScoreCounts) {
        return score << doubleScoreCounts;
    }
}
