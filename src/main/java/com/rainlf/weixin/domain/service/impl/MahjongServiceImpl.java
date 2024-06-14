package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.MahjongRecordInfo;
import com.rainlf.weixin.domain.model.MahjongRecordType;
import com.rainlf.weixin.domain.service.MahjongService;
import com.rainlf.weixin.infra.db.model.MahjongRecord;
import com.rainlf.weixin.infra.db.model.User;
import com.rainlf.weixin.infra.db.model.UserAsset;
import com.rainlf.weixin.infra.db.repository.MahjongRecordRepository;
import com.rainlf.weixin.infra.db.repository.UserAssetRepository;
import com.rainlf.weixin.infra.db.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecord(MahjongRecordInfo mahjongRecordInfo) {
        boolean game = Objects.equals(mahjongRecordInfo.getType(), MahjongRecordType.GAME);
        Set<Integer> userIds = mahjongRecordInfo.getRecords().stream().map(MahjongRecordInfo.Record::getUserId).collect(Collectors.toSet());
        if (game) {
            userIds.add(mahjongRecordInfo.getRecorderId());
        }

        List<UserAsset> userAssetList = userAssetRepository.findByUserIdIn(userIds);
        Map<Integer, UserAsset> userAssetMap = userAssetList.stream().collect(Collectors.toMap(UserAsset::getUserId, userAsset -> userAsset));

        // modify user asset
        mahjongRecordInfo.getRecords().forEach(record -> {
            UserAsset userAsset = userAssetMap.get(record.getUserId());
            log.info("saveRecord: userId: {}, score: {}", userAsset.getUserId(), record.getScore());
            userAsset.setCopperCoin(userAsset.getCopperCoin() + record.getScore());
        });
        // award recoder
        int recorderAward = 0;
        if (game) {
            recorderAward = new Random().nextInt(randomMax) + 1;
            UserAsset recorderAsset = userAssetMap.get(mahjongRecordInfo.getRecorderId());
            log.info("saveRecord: userId: {}, award: {}", mahjongRecordInfo.getRecorderId(), recorderAward);
            recorderAsset.setCopperCoin(recorderAsset.getCopperCoin() + recorderAward);
        }
        // save db
        userAssetRepository.saveAll(userAssetList);
        log.info("saveRecord: save user asset");

        // save record
        String roundId = UUID.randomUUID().toString().replaceAll("-", "");
        List<MahjongRecord> mahjongRecordList = mahjongRecordInfo.getRecords()
                .stream()
                .map(record -> {
                    MahjongRecord mahjongRecord = new MahjongRecord();
                    mahjongRecord.setRoundId(roundId);
                    mahjongRecord.setRecorderId(mahjongRecordInfo.getRecorderId());
                    mahjongRecord.setType(mahjongRecordInfo.getType().toString());
                    mahjongRecord.setUserId(record.getUserId());
                    mahjongRecord.setScore(record.getScore());
                    return mahjongRecord;
                }).collect(Collectors.toList());

        // add record award record
        if (game) {
            MahjongRecord mahjongRecord = new MahjongRecord();
            mahjongRecord.setRoundId(roundId);
            mahjongRecord.setRecorderId(mahjongRecordInfo.getRecorderId());
            mahjongRecord.setType(MahjongRecordType.AWARD.toString());
            mahjongRecord.setUserId(mahjongRecordInfo.getRecorderId());
            mahjongRecord.setScore(recorderAward);
            mahjongRecordList.add(mahjongRecord);
        }

        mahjongRecordRepository.saveAll(mahjongRecordList);
        log.info("saveRecord: save mahjong record");
    }

    @Override
    public List<MahjongRecordInfo> getRecords() {
        List<MahjongRecordInfo> result = new ArrayList<>();
        List<MahjongRecord> allRecordList = mahjongRecordRepository.findAll();

        // find user info
        Set<Integer> userIdList = allRecordList.stream().map(MahjongRecord::getUserId).collect(Collectors.toSet());
        userIdList.addAll(allRecordList.stream().map(MahjongRecord::getRecorderId).collect(Collectors.toSet()));
        List<User> userList = userRepository.findAllById(userIdList);

        // handle record by group
        Map<String, List<MahjongRecord>> allRecordGroupMap = allRecordList.stream().collect(Collectors.groupingBy(MahjongRecord::getRoundId));
        for (List<MahjongRecord> roundList : allRecordGroupMap.values()) {
            List<MahjongRecordInfo> resultItem = new ArrayList<>();

            // create record award info
            List<MahjongRecord> awardRecord = roundList.stream().filter(x -> Objects.equals(MahjongRecordType.fromString(x.getType()), MahjongRecordType.AWARD)).toList();
            if (!awardRecord.isEmpty()) {
                resultItem.add(createMahjongRecordInfo(awardRecord, userList));
            }

            // create game or sport info
            List<MahjongRecord> noAwardList = roundList.stream().filter(x -> !Objects.equals(MahjongRecordType.fromString(x.getType()), MahjongRecordType.AWARD)).toList();
            resultItem.add(createMahjongRecordInfo(noAwardList, userList));
        }
        return result;
    }

    private MahjongRecordInfo createMahjongRecordInfo(List<MahjongRecord> mahjongRecords, List<User> users) {
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        // the same info
        MahjongRecord mahjongRecord = mahjongRecords.get(0);
        User recordUser = userMap.get(mahjongRecord.getRecorderId());

        MahjongRecordInfo mahjongRecordInfo = new MahjongRecordInfo();
        mahjongRecordInfo.setType(MahjongRecordType.fromString(mahjongRecord.getType()));
        mahjongRecordInfo.setRecorderId(recordUser.getId());
        mahjongRecordInfo.setRecorderName(recordUser.getNickname());
        mahjongRecordInfo.setRecorderAvatar(recordUser.getAvatar());
        mahjongRecordInfo.setCreateTime(mahjongRecord.getCreateTime());

        // record detail of echo user
        List<MahjongRecordInfo.Record> records = mahjongRecords.stream()
                .map(x -> {
                            User user = userMap.get(x.getUserId());
                            MahjongRecordInfo.Record record = new MahjongRecordInfo.Record();
                            record.setUserId(user.getId());
                            record.setUserName(user.getNickname());
                            record.setUserAvatar(user.getAvatar());
                            record.setScore(x.getScore());
                            return record;
                        }
                ).toList();
        mahjongRecordInfo.setRecords(records);
        return mahjongRecordInfo;
    }
}
