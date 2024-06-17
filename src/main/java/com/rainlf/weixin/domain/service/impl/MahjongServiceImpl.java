package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.MahjongRecordDto;
import com.rainlf.weixin.app.dto.RoundInfoDto;
import com.rainlf.weixin.app.dto.SportInfoDto;
import com.rainlf.weixin.domain.model.MahjongDetailType;
import com.rainlf.weixin.domain.model.MahjongSiteType;
import com.rainlf.weixin.domain.service.MahjongService;
import com.rainlf.weixin.infra.db.model.MahjongRound;
import com.rainlf.weixin.infra.db.model.MahjongRoundDetail;
import com.rainlf.weixin.infra.db.model.User;
import com.rainlf.weixin.infra.db.model.UserAsset;
import com.rainlf.weixin.infra.db.repository.MahjongRoundDetailRepository;
import com.rainlf.weixin.infra.db.repository.MahjongRoundRepository;
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
public class MahjongServiceImpl implements MahjongService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAssetRepository userAssetRepository;
    @Autowired
    private MahjongRoundRepository mahjongRoundRepository;
    @Autowired
    private MahjongRoundDetailRepository mahjongRoundDetailRepository;

    @Value("${recorder.award.random.max")
    private int randomMax;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecord(RoundInfoDto roundInfoDto) {
        MahjongRound mahjongRound = new MahjongRound();
        mahjongRound.setRecorderId(roundInfoDto.getRecorderId());
        mahjongRound.setBaseFan(roundInfoDto.getBaseFan());
        mahjongRound.setWinType(roundInfoDto.getWinType().toString());
        mahjongRound.setFanTypes(JsonUtils.toJson(roundInfoDto.getFanTypes()));
        mahjongRound = mahjongRoundRepository.save(mahjongRound);
        roundInfoDto.setRoundId(mahjongRound.getId());

        int fan = calculateTotalFan(roundInfoDto.getBaseFan(), roundInfoDto.getFanTypes().size());

        List<User> users = new ArrayList<>();
        Map<Integer, Integer> socreMap = new HashMap<>();
        List<User> winners = userRepository.findAllById(roundInfoDto.getWinnerIds());
        List<User> losers = userRepository.findAllById(roundInfoDto.getLoserIds());

        int winerNumber = roundInfoDto.getWinType().getWinnerNumber();
        int loserNumber = roundInfoDto.getWinType().getLoserNumber();
        if (winners.size() != winerNumber || losers.size() != loserNumber) {
            throw new RuntimeException("not expect scenario");
        }

        users.addAll(winners);
        users.addAll(losers);
        winners.forEach(winner -> socreMap.put(winner.getId(), fan * loserNumber));
        losers.forEach(loser -> socreMap.put(loser.getId(), -fan * winerNumber));
        log.info("socre info: {}", socreMap);

        saveRoundDetail(users, socreMap, roundInfoDto.getRoundId(), roundInfoDto.getSiteMap());
        log.info("save round detail success");

        int award = new Random().nextInt(randomMax) + 1;
        saveRecorderDetail(mahjongRound.getId(), roundInfoDto.getRecorderId(), award, MahjongDetailType.RECORD);
        log.info("save recorder detail success");
    }


    @Override
    public void saveSportInfo(SportInfoDto sportInfoDto) {
        saveRecorderDetail(null, sportInfoDto.getSportNumber(), sportInfoDto.getSportNumber(), MahjongDetailType.SPORT);
        log.info("save recorder detail success");
    }

    @Override
    public List<MahjongRecordDto> getRecords(Integer pageNumber, Integer pageSize) {

        return null;
    }

    private void saveRecorderDetail(Integer roundId, Integer userId, Integer score, MahjongDetailType type) {
        User user = userRepository.findById(userId).orElseThrow();
        UserAsset userAsset = userAssetRepository.findByUserId(userId).orElseThrow();

        userAsset.setCopperCoin(userAsset.getCopperCoin() + score);
        userAssetRepository.save(userAsset);

        MahjongRoundDetail detail = new MahjongRoundDetail();
        detail.setRoundId(roundId);
        detail.setType(type.toString());
        detail.setUserId(userId);
        detail.setScore(score);
        mahjongRoundDetailRepository.save(detail);
    }

    private void saveRoundDetail(List<User> users, Map<Integer, Integer> socreMap, Integer roundId, Map<Integer, MahjongSiteType> siteMap) {
        // find asset
        List<UserAsset> userAssets = userAssetRepository.findByUserIdIn(users.stream().map(User::getId).toList());
        Map<Integer, UserAsset> userAssetMap = userAssets.stream().collect(Collectors.toMap(UserAsset::getUserId, x -> x));

        // change asset and insert round details
        List<MahjongRoundDetail> mahjongRoundDetails = new ArrayList<>();
        users.forEach(user -> {
            Integer userId = user.getId();
            // change user asset
            UserAsset userAsset = userAssetMap.get(userId);
            userAsset.setCopperCoin(userAsset.getCopperCoin() + socreMap.get(userId));

            // insert round details
            MahjongRoundDetail detail = new MahjongRoundDetail();
            detail.setRoundId(roundId);
            detail.setType(MahjongDetailType.GAME.toString());
            detail.setUserId(userId);
            detail.setScore(socreMap.get(userId));
            detail.setSite(siteMap.get(userId).toString());
            mahjongRoundDetails.add(detail);
        });
        userAssetRepository.saveAll(userAssets);
        mahjongRoundDetailRepository.saveAll(mahjongRoundDetails);
    }

    private int calculateTotalFan(int baseFan, int doubleFanCounts) {
        int fan = baseFan + 1;
        fan = fan << (doubleFanCounts + 1);
        return fan;
    }

}
