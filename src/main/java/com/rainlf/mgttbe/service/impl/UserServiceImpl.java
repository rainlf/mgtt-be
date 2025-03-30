package com.rainlf.mgttbe.service.impl;

import com.rainlf.mgttbe.infra.db.manager.MaJiangGameItemManager;
import com.rainlf.mgttbe.infra.db.manager.MaJiangGameManager;
import com.rainlf.mgttbe.infra.db.manager.MgttConfigManager;
import com.rainlf.mgttbe.infra.db.manager.UserManager;
import com.rainlf.mgttbe.infra.db.repository.MgttConfigRepository;
import com.rainlf.mgttbe.infra.util.DateUtils;
import com.rainlf.mgttbe.infra.util.JsonUtils;
import com.rainlf.mgttbe.model.*;
import com.rainlf.mgttbe.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MaJiangGameManager maJiangGameManager;
    @Autowired
    private MaJiangGameItemManager maJiangGameItemManager;
    @Autowired
    private MgttConfigManager mgttConfigManager;


    @Override
    public User login(String code) {
        String appId = mgttConfigManager.getWxAppId();
        String appSecret = mgttConfigManager.getWxAppSecret();
        String api = mgttConfigManager.getWxLoginUrl();
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", api, appId, appSecret, code);

        String response = restTemplate.getForObject(url, String.class);
        log.info("login, response: {}", response);

        WeixinSession weixinSession = JsonUtils.readString(response, WeixinSession.class);
        if (weixinSession.invalid()) {
            throw new RuntimeException(weixinSession.getErrmsg());
        }

        User user = userManager.findByOpenId(weixinSession.getOpenId());
        if (user == null) {
            log.info("login, user not found, register user");
            user = new User();
            user.setOpenId(weixinSession.getOpenId());
            user.setSessionKey(weixinSession.getSessionKey());
            user.setLastLoginTime(DateUtils.now());
            user.setPoints(0);
            user = userManager.save(user);
        } else {
            log.info("login, user exists, username: {}", user.getUsername());
            user.setSessionKey(weixinSession.getSessionKey());
            user.setLastLoginTime(DateUtils.now());
            user = userManager.save(user);
        }

        return user;
    }

    @Override
    public User findUserById(Integer id) {
        return userManager.findById(id);
    }

    @Override
    public List<User> save(List<User> users) {
        return userManager.save(users);
    }

    @Override
    public User save(User user) {
        return userManager.save(user);
    }

    @Override
    public User save(Integer userId, String userName) {
        User user = userManager.findById(userId);
        user.setUsername(userName);
        user = userManager.save(user);
        return user;
    }

    @Override
    public User save(Integer userId, String userName, byte[] avatar) {
        User user = userManager.findById(userId);
        user.setUsername(userName);
        user.setAvatar(avatar);
        user = userManager.save(user);
        return user;
    }

    @Override
    public int updateUserPoint(Integer userId, Integer points) {
        return userManager.updateUserPoint(userId, points);
    }

    @Override
    public List<User> getUserRank() {
        List<User> users = userManager.findAllUsers();

        for (User user : users) {
            // find last 20 games
            List<MaJiangGame> games = maJiangGameManager.findLastGamesByUser(user.getId(), 20);
            List<MaJiangGameInfo> gameInfos = new ArrayList<>();
            games.forEach(game -> {
                List<MaJiangGameItem> items = maJiangGameItemManager.findByGameId(game.getId());
                MaJiangGameInfo gameinfo = new MaJiangGameInfo();
                gameinfo.setGame(game);
                gameinfo.setItems(items);
                gameInfos.add(gameinfo);
            });

            Integer userId = user.getId();
            List<String> tags = new ArrayList<>();
            gameInfos.forEach(gameinfo -> {
                MaJiangGameItem gameItem = gameinfo.findUserGameItem(userId);
                // winner or loser
                if (gameItem != null) {
                    // winner
                    if (gameItem.getType() == MaJiangUserType.WINNER) {
                        // 自摸
                        if (gameinfo.getGame().getType() == MaJiangGameType.ZI_MO) {
                            tags.add(gameinfo.getGame().getType().getName());
                        }
                        // 赢家牌型
                        tags.addAll(gameItem.getWinTypes().stream().map(MaJiangWinType::getName).toList());
                    }
                    // loser
                    if (gameItem.getType() == MaJiangUserType.LOSER) {
                        if (gameinfo.getGame().getType() == MaJiangGameType.YI_PAO_SHUANG_XIANG || gameinfo.getGame().getType() == MaJiangGameType.YI_PAO_SAN_XIANG) {
                            tags.add(gameinfo.getGame().getType().getName());
                        }
                    }
                }
            });
            user.setLastTags(tags.stream().distinct().collect(Collectors.toList()));
        }

        List<User> zeroUser = users.stream().filter(x -> x.getPoints() == 0).toList();
        List<User> nonZeroUser = users.stream().filter(x -> x.getPoints() != 0).toList();
        List<User> sortedUser = new ArrayList<>(nonZeroUser);
        sortedUser.sort(Comparator.comparing(User::getPoints).reversed());
        sortedUser.addAll(zeroUser);
        return sortedUser;
    }

    @Override
    public List<User> findAllUsers() {
        return userManager.findAllUsers();
    }
}
