package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.MahjongGameRecord;
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
        Set<Integer> userIds = mahjongGameRecord.getRecords().stream().map(MahjongGameRecord.Record::getUserId).collect(Collectors.toSet());
        userIds.add(mahjongGameRecord.getRecorderId());

        List<UserAsset> userAssetList = userAssetRepository.findByUserIdIn(userIds);
        Map<Integer, UserAsset> userAssetMap = userAssetList.stream().collect(Collectors.toMap(UserAsset::getId, userAsset -> userAsset));

        // modify user asset
        mahjongGameRecord.getRecords().forEach(record -> {
            UserAsset userAsset = userAssetMap.get(record.getUserId());
            userAsset.setCopperCoin(userAsset.getCopperCoin() + record.getScore());
        });
        // award recoder
        Integer recorderAward = new Random().nextInt(randomMax) + 1;
        UserAsset recorderAsset = userAssetMap.get(mahjongGameRecord.getRecorderId());
        recorderAsset.setCopperCoin(recorderAsset.getCopperCoin() + recorderAward);
        // save db
        userAssetRepository.saveAll(userAssetList);

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
                }).toList();

        // add record award record
        MahjongRecord mahjongRecord = new MahjongRecord();
        mahjongRecord.setRoundId(roundId);
        mahjongRecord.setRecorderId(mahjongGameRecord.getRecorderId());
        mahjongRecord.setType(mahjongGameRecord.getType().toString());
        mahjongRecord.setUserId(mahjongGameRecord.getRecorderId());
        mahjongRecord.setScore(recorderAward);
        mahjongRecordList.add(mahjongRecord);
        mahjongRecordRepository.saveAll(mahjongRecordList);
    }
}
