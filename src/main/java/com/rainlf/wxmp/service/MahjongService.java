package com.rainlf.wxmp.service;

import com.rainlf.wxmp.dao.MahjongLog;
import com.rainlf.wxmp.dao.MahjongLogRepo;
import com.rainlf.wxmp.dao.MahjongUser;
import com.rainlf.wxmp.dao.MahjongUserRepo;
import com.rainlf.wxmp.model.UserAssetsChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author rain
 * @date 4/20/2024 9:20 PM
 */
@Service
public class MahjongService {
    @Autowired
    private MahjongUserRepo mahjongUserRepo;
    @Autowired
    private MahjongLogRepo mahjongLogRepo;

    private final String BANK = "银行";


    public List<MahjongUser> findAllUser() {
        List<MahjongUser> users = mahjongUserRepo.findAll();
        return reorderUser(users);
    }

    public void updateUser(UserAssetsChange userAssetsChange) {
        MahjongUser user = mahjongUserRepo.findByName(userAssetsChange.getName());
        user.setAssets(user.getAssets() + userAssetsChange.getAssetsChange());
        mahjongUserRepo.save(user);
    }

    private List<MahjongUser> reorderUser(List<MahjongUser> users) {
        List<MahjongUser> noBankUsers = users.stream().filter(x -> !Objects.equals(x.getName(), BANK)).toList();
        MahjongUser bank = users.stream().filter(x -> Objects.equals(x.getName(), BANK)).findFirst().orElse(null);

        List<MahjongUser> zeroUsers = noBankUsers.stream().filter(x -> x.getAssets() == 0).toList();
        List<MahjongUser> noZeroUsers = noBankUsers.stream().filter(x -> x.getAssets() != 0).sorted((a, b) -> b.getAssets() - a.getAssets()).toList();

        List<MahjongUser> result = new ArrayList<>();
        result.addAll(noZeroUsers);
        result.addAll(zeroUsers);
        if (Objects.nonNull(bank)) {
            result.add(bank);
        }
        return result;
    }

    public List<String> findAllLog() {
        List<MahjongLog> logs = mahjongLogRepo.findAll();
        return logs.stream().sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime())).map(MahjongLog::getMessage).toList();
    }

    public void insertLog(String message) {
        MahjongLog log = new MahjongLog();
        log.setMessage(message);
        mahjongLogRepo.save(log);
    }
}
