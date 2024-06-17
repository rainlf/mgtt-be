package com.rainlf.weixin.domain.service.impl;

import com.rainlf.weixin.app.dto.MahjongRecord;
import com.rainlf.weixin.app.dto.RoundInfo;
import com.rainlf.weixin.domain.model.RecordType;
import com.rainlf.weixin.domain.service.MahjongService;
import com.rainlf.weixin.infra.db.model.MahjongRound;
import com.rainlf.weixin.infra.db.model.User;
import com.rainlf.weixin.infra.db.model.UserAsset;
import com.rainlf.weixin.infra.db.repository.MahjongRecordRepository;
import com.rainlf.weixin.infra.db.repository.UserAssetRepository;
import com.rainlf.weixin.infra.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public void saveRecord(RoundInfo roundInfo) {
        boolean game = Objects.equals(mahjongRecordLog.getType(), RecordType.GAME);
        Set<Integer> userIds = mahjongRecordLog.getRecordItems().stream().map(MahjongRecord.RecordItem::getUserId).collect(Collectors.toSet());
        if (game) {
            userIds.add(mahjongRecordLog.getRecorderId());
        }

        List<UserAsset> userAssetList = userAssetRepository.findByUserIdIn(userIds);
        Map<Integer, UserAsset> userAssetMap = userAssetList.stream().collect(Collectors.toMap(UserAsset::getUserId, userAsset -> userAsset));

        // modify user asset
        mahjongRecordLog.getRecordItems().forEach(recordItem -> {
            UserAsset userAsset = userAssetMap.get(recordItem.getUserId());
            log.info("saveRecord: userId: {}, score: {}", userAsset.getUserId(), recordItem.getScore());
            userAsset.setCopperCoin(userAsset.getCopperCoin() + recordItem.getScore());
        });
        // award recoder
        int recorderAward = 0;
        if (game) {
            recorderAward = new Random().nextInt(randomMax) + 1;
            UserAsset recorderAsset = userAssetMap.get(mahjongRecordLog.getRecorderId());
            log.info("saveRecord: userId: {}, award: {}", mahjongRecordLog.getRecorderId(), recorderAward);
            recorderAsset.setCopperCoin(recorderAsset.getCopperCoin() + recorderAward);
        }
        // save db
        userAssetRepository.saveAll(userAssetList);
        log.info("saveRecord: save user asset");

        // save record
        String roundId = UUID.randomUUID().toString().replaceAll("-", "");
        List<MahjongRound> mahjongRoundList = mahjongRecordLog.getRecordItems()
                .stream()
                .map(recordItem -> {
                    MahjongRound mahjongRound = new MahjongRound();
                    mahjongRound.setRoundId(roundId);
                    mahjongRound.setRecorderId(mahjongRecordLog.getRecorderId());
                    mahjongRound.setType(mahjongRecordLog.getType().toString());
                    mahjongRound.setUserId(recordItem.getUserId());
                    mahjongRound.setScore(recordItem.getScore());
                    return mahjongRound;
                }).collect(Collectors.toList());

        // add record award record
        if (game) {
            MahjongRound mahjongRound = new MahjongRound();
            mahjongRound.setRoundId(roundId);
            mahjongRound.setRecorderId(mahjongRecordLog.getRecorderId());
            mahjongRound.setType(RecordType.AWARD.toString());
            mahjongRound.setUserId(mahjongRecordLog.getRecorderId());
            mahjongRound.setScore(recorderAward);
            mahjongRoundList.add(mahjongRound);
        }

        mahjongRecordRepository.saveAll(mahjongRoundList);
        log.info("saveRecord: save mahjong record");
    }

    @Override
    public List<MahjongRecord> getRecords(Integer pageNumber, Integer pageSize) {
        List<MahjongRecord> result = new ArrayList<>();

        // find record pageable
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<MahjongRound> allRecordPage = mahjongRecordRepository.findAll(pageable);
        List<MahjongRound> allRecordList = allRecordPage.getContent();

        // find user info
        Set<Integer> userIdList = allRecordList.stream().map(MahjongRound::getUserId).collect(Collectors.toSet());
        userIdList.addAll(allRecordList.stream().map(MahjongRound::getRecorderId).collect(Collectors.toSet()));
        List<User> userList = userRepository.findAllById(userIdList);

        // handle record by group
        Map<String, List<MahjongRound>> allRecordGroupMap = allRecordList.stream().collect(Collectors.groupingBy(MahjongRound::getRoundId));
        for (List<MahjongRound> roundList : allRecordGroupMap.values()) {

            // create record award info
            List<MahjongRound> awardRecord = roundList.stream().filter(x -> Objects.equals(RecordType.fromString(x.getType()), RecordType.AWARD)).toList();
            if (!awardRecord.isEmpty()) {
                result.add(createMahjongRecordInfo(awardRecord, userList));
            }

            // create game or sport info
            List<MahjongRound> noAwardList = roundList.stream().filter(x -> !Objects.equals(RecordType.fromString(x.getType()), RecordType.AWARD)).toList();
            result.add(createMahjongRecordInfo(noAwardList, userList));
        }
        return result;
    }

    private MahjongRecord createMahjongRecordInfo(List<MahjongRound> mahjongRounds, List<User> users) {
        Map<Integer, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        // the same info
        MahjongRound mahjongRound = mahjongRounds.get(0);
        User recordUser = userMap.get(mahjongRound.getRecorderId());

        MahjongRecord mahjongRecordLog = new MahjongRecord();
        mahjongRecordLog.setType(RecordType.fromString(mahjongRound.getType()));
        mahjongRecordLog.setRecorderId(recordUser.getId());
        mahjongRecordLog.setRecorderName(recordUser.getNickname());
        mahjongRecordLog.setRecorderAvatar(recordUser.getAvatar());
        mahjongRecordLog.setCreateTime(mahjongRound.getCreateTime());

        // record detail of echo user
        List<MahjongRecord.RecordItem> recordItems = mahjongRounds.stream()
                .map(x -> {
                            User user = userMap.get(x.getUserId());
                            MahjongRecord.RecordItem recordItem = new MahjongRecord.RecordItem();
                            recordItem.setUserId(user.getId());
                            recordItem.setUserName(user.getNickname());
                            recordItem.setUserAvatar(user.getAvatar());
                            recordItem.setScore(x.getScore());
                            return recordItem;
                        }
                ).toList();
        mahjongRecordLog.setRecordItems(recordItems);
        return mahjongRecordLog;
    }
}
