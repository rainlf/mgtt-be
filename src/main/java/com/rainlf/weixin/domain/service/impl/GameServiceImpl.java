package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.MahjongRecordDto;
import com.rainlf.weixin.app.dto.MahjongRoundInfoDto;
import com.rainlf.weixin.app.dto.SportInfoDto;
import com.rainlf.weixin.domain.consts.GameDetailTypeEnum;
import com.rainlf.weixin.domain.consts.GameTypeEnum;
import com.rainlf.weixin.domain.service.GameService;
import com.rainlf.weixin.infra.db.model.*;
import com.rainlf.weixin.infra.db.repository.*;
import com.rainlf.weixin.infra.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private MahjongPlayerRepository mahjongPlayerRepository;

    @Value("${recorder.award.random.max}")
    private int randomAwardMax;


    @Override
    public void addMahjongPlayer(Integer id) {
        List<MahjongPlayer> mahjongPlayers = mahjongPlayerRepository.findByUserId(id);
        mahjongPlayers.forEach(x -> x.setDeleted(true));

        MahjongPlayer mahjongPlayer = new MahjongPlayer();
        mahjongPlayer.setUserId(id);
        mahjongPlayers.add(mahjongPlayer);

        mahjongPlayerRepository.saveAll(mahjongPlayers);
    }

    @Override
    public void deleteMahjongPlayer(Integer id) {
        List<MahjongPlayer> mahjongPlayers = mahjongPlayerRepository.findByUserId(id);
        mahjongPlayers.forEach(x -> x.setDeleted(true));
        mahjongPlayerRepository.saveAll(mahjongPlayers);
    }

    @Override
    public List<Integer> getMahjongPlayerIds() {
        List<MahjongPlayer> mahjongPlayers = mahjongPlayerRepository.findAll(Sort.by(Sort.Direction.ASC, "createTime"));
        return mahjongPlayers.stream().map(MahjongPlayer::getUserId).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMahjongInfo(MahjongRoundInfoDto mahjongRoundInfoDto) {
        Game game = new Game();
        game.setGameType(GameTypeEnum.MAHJONG);
        game.setRecorderId(mahjongRoundInfoDto.getRecorderId());
        game.setScore(mahjongRoundInfoDto.getBaseFan());
        game.setWinCase(mahjongRoundInfoDto.getWinCase().toString());
        game.setScoreExt(JsonUtils.toJson(mahjongRoundInfoDto.getFanList()));
        game = gameRepository.save(game);

        int fan = calculateMahjongTotalFan(mahjongRoundInfoDto.getBaseFan(), mahjongRoundInfoDto.getFanList().size());
        log.info("total fan: {}", fan);

        List<User> users = new ArrayList<>();
        Map<Integer, Integer> socreMap = new HashMap<>();
        List<User> winners = userRepository.findAllById(mahjongRoundInfoDto.getWinnerIds());
        List<User> losers = userRepository.findAllById(mahjongRoundInfoDto.getLoserIds());

        int winnerNumber = mahjongRoundInfoDto.getWinCase().getWinnerNumber();
        int loserNumber = mahjongRoundInfoDto.getWinCase().getLoserNumber();
        if (winners.size() != winnerNumber || losers.size() != loserNumber) {
            throw new RuntimeException("not expect scenario");
        }

        users.addAll(winners);
        users.addAll(losers);
        winners.forEach(winner -> socreMap.put(winner.getId(), fan * loserNumber));
        losers.forEach(loser -> socreMap.put(loser.getId(), -fan * winnerNumber));
        log.info("socre info: {}", socreMap);

        savePlayerDetail(game.getId(), users, socreMap);
        log.info("save game detail success");

        int award = new Random().nextInt(randomAwardMax) + 1;
        log.info("recorder info, userId: {}, score: {}", mahjongRoundInfoDto.getRecorderId(), award);
        saveRecorderDetail(game.getId(), mahjongRoundInfoDto.getRecorderId(), award);
        log.info("save recorder detail success");
    }


    @Override
    public void saveSportInfo(SportInfoDto sportInfoDto) {
        log.info("sport info, userId: {}, score: {}", sportInfoDto.getSporterId(), sportInfoDto.getSportNumber());
        saveSporterDetail(sportInfoDto.getSporterId(), sportInfoDto.getSportNumber());
        log.info("save sporter detail success");
    }

    @Override
    public List<MahjongRecordDto> getMahjongRecords(Integer pageNumber, Integer pageSize) {
        return null;
    }

    /**
     * change recorder asset and save game detail
     */
    private void savePlayerDetail(Integer gameId, List<User> users, Map<Integer/* userId */, Integer/* userScore */> socreMap) {
        saveMultiGameDetail(gameId, GameDetailTypeEnum.MAHJONG_GAME, users, socreMap);
    }

    /**
     * change recorder asset and save game detail
     */
    private void saveRecorderDetail(Integer gameId, Integer userId, Integer score) {
        saveSingleGameDetail(gameId, GameDetailTypeEnum.MAHJONG_AWARD, userId, score);
    }

    /**
     * change sporter asset and save game detail
     */
    private void saveSporterDetail(Integer userId, Integer score) {
        saveSingleGameDetail(null, GameDetailTypeEnum.REPAYMENT_SPORT, userId, score);
    }

    /**
     * change single user asset and save game detail
     */
    private void saveSingleGameDetail(Integer gameId, GameDetailTypeEnum type, Integer userId, Integer score) {
        User user = userRepository.findById(userId).orElseThrow();
        List<User> users = Collections.singletonList(user);
        Map<Integer, Integer> socreMap = new HashMap<>();
        socreMap.put(user.getId(), score);
        saveMultiGameDetail(gameId, type, users, socreMap);
    }

    /**
     * change multi user asset and save game detail
     */
    private void saveMultiGameDetail(Integer gameId, GameDetailTypeEnum type, List<User> users, Map<Integer/* userId */, Integer/* userScore */> socreMap) {
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
            detail.setUserId(userId);
            detail.setType(type);
            detail.setScore(socreMap.get(userId));
            gameDetails.add(detail);
        });

        // save db
        userAssetRepository.saveAll(userAssets);
        gameDetailRepository.saveAll(gameDetails);
    }

    /**
     * calculate mahjong total fan
     */
    private int calculateMahjongTotalFan(int fan, int fanListSize) {
        return fan << fanListSize;
    }
}
