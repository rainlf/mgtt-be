package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.MahjongGameRecord;
import com.rainlf.weixin.domain.model.MahjongRecordType;
import com.rainlf.weixin.domain.service.MahjongService;
import com.rainlf.weixin.infra.db.model.MahjongRecord;
import com.rainlf.weixin.infra.db.model.UserAsset;
import com.rainlf.weixin.infra.db.repository.MahjongRecordRepository;
import com.rainlf.weixin.infra.db.repository.UserAssetRepository;
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
    private MahjongRecordRepository mahjongRecordRepository;
    @Autowired
    private UserAssetRepository userAssetRepository;

    @Value("${recorder.award.random.max}")
    private int randomMax;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecord(MahjongGameRecord mahjongGameRecord) {
        boolean game = Objects.equals(mahjongGameRecord.getType(), MahjongRecordType.GAME);
        Set<Integer> userIds = mahjongGameRecord.getRecords().stream().map(MahjongGameRecord.Record::getUserId).collect(Collectors.toSet());
        if (game) {
            userIds.add(mahjongGameRecord.getRecorderId());
        }

        List<UserAsset> userAssetList = userAssetRepository.findByUserIdIn(userIds);
        Map<Integer, UserAsset> userAssetMap = userAssetList.stream().collect(Collectors.toMap(UserAsset::getUserId, userAsset -> userAsset));

        // modify user asset
        mahjongGameRecord.getRecords().forEach(record -> {
            UserAsset userAsset = userAssetMap.get(record.getUserId());
            log.info("saveRecord: userId: {}, score: {}", userAsset.getUserId(), record.getScore());
            userAsset.setCopperCoin(userAsset.getCopperCoin() + record.getScore());
        });
        // award recoder
        int recorderAward = 0;
        if (game) {
            recorderAward = new Random().nextInt(randomMax) + 1;
            UserAsset recorderAsset = userAssetMap.get(mahjongGameRecord.getRecorderId());
            log.info("saveRecord: userId: {}, award: {}", mahjongGameRecord.getRecorderId(), recorderAward);
            recorderAsset.setCopperCoin(recorderAsset.getCopperCoin() + recorderAward);
        }
        // save db
        userAssetRepository.saveAll(userAssetList);
        log.info("saveRecord: save user asset");

        // save record
        String roundId = UUID.randomUUID().toString().replaceAll("-", "");
        List<MahjongRecord> mahjongRecordList = mahjongGameRecord.getRecords()
                .stream()
                .map(record -> {
                    MahjongRecord mahjongRecord = new MahjongRecord();
                    mahjongRecord.setRoundId(roundId);
                    mahjongRecord.setRecorderId(mahjongGameRecord.getRecorderId());
                    mahjongRecord.setType(mahjongGameRecord.getType().toString());
                    mahjongRecord.setUserId(record.getUserId());
                    mahjongRecord.setScore(record.getScore());
                    return mahjongRecord;
                }).collect(Collectors.toList());

        // add record award record
        if (game) {
            MahjongRecord mahjongRecord = new MahjongRecord();
            mahjongRecord.setRoundId(roundId);
            mahjongRecord.setRecorderId(mahjongGameRecord.getRecorderId());
            mahjongRecord.setType(MahjongRecordType.AWARD.toString());
            mahjongRecord.setUserId(mahjongGameRecord.getRecorderId());
            mahjongRecord.setScore(recorderAward);
            mahjongRecordList.add(mahjongRecord);
        }

        mahjongRecordRepository.saveAll(mahjongRecordList);
        log.info("saveRecord: save mahjong record");
    }
}
