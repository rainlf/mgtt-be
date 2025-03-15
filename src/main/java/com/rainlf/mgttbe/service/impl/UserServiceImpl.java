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
            // find last 10 games
            List<MaJiangGame> maJiangGames = maJiangGameManager.findLastGamesByUserAndDays(user.getId(), 10);
            Map<Integer, MaJiangGame> maJiangGameMap = maJiangGames.stream().collect(Collectors.toMap(MaJiangGame::getId, x -> x, (k1, k2) -> k1));
            List<Integer> gameIds = maJiangGames.stream().map(MaJiangGame::getId).toList();

            // find relation game items
            List<MaJiangGameItem> maJiangGameItems = maJiangGameItemManager.findByGameIdAndUserId(gameIds, user.getId());

            // find winner & loser game items
            List<MaJiangGameItem> playerItems = maJiangGameItems
                    .stream()
                    .filter(x -> Objects.equals(x.getType(), MaJiangUserType.WINNER) || Objects.equals(x.getType(), MaJiangUserType.LOSER))
                    .toList();

            List<String> tags = new ArrayList<>();
            playerItems.forEach(x -> {
                MaJiangGame maJiangGame = maJiangGameMap.get(x.getGameId());
                if (!Objects.equals(maJiangGame.getType(), MaJiangGameType.PING_HU)) {
                    tags.add(maJiangGame.getType().getName());
                }
            });
            tags.addAll(playerItems.stream().filter(x -> x.getWinTypes() != null).flatMap(x -> x.getWinTypes().stream()).map(MaJiangWinType::getName).toList());
            user.setLastTags(tags.stream().distinct().collect(Collectors.toList()));
        }

        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort(Comparator.comparing(User::getPoints).reversed());
        return sortedUsers;
    }

    @Override
    public List<User> findAllUsers() {
        return userManager.findAllUsers();
    }
}
