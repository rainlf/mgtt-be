package com.rainlf.mgttbe.infra.db.manager;

import com.rainlf.mgttbe.infra.db.dataobj.MaJiangGameDO;
import com.rainlf.mgttbe.infra.db.repository.MaJiangGameRepository;
import com.rainlf.mgttbe.model.MaJiangGame;
import com.rainlf.mgttbe.model.MaJiangGameType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaJiangGameManager {
    @Autowired
    private MaJiangGameRepository majiangGameRepository;

    public MaJiangGame save(MaJiangGame majiangGame) {
        return toMaJiangGame(majiangGameRepository.save(toMaJiangGameDO(majiangGame)));
    }


    public MaJiangGame findByIdWithLock(Integer id) {
        return toMaJiangGame(majiangGameRepository.findByIdWithLock(id));
    }

    public List<MaJiangGame> findByIdIn(List<Integer> ids) {
        return majiangGameRepository.findByIdIn(ids).stream().map(this::toMaJiangGame).toList();
    }

    public MaJiangGame findById(Integer id) {
        return majiangGameRepository.findById(id).map(this::toMaJiangGame).orElse(null);
    }

    public List<MaJiangGame> findLastGames(Integer limit) {
        return majiangGameRepository.findLastGames(limit)
                .stream()
                .map(this::toMaJiangGame)
                .collect(Collectors.toList());
    }

    public List<MaJiangGame> findLastGamesByUser(Integer userId, Integer limit) {
        return majiangGameRepository.findLastGamesByUser(userId, limit)
                .stream()
                .map(this::toMaJiangGame)
                .collect(Collectors.toList());
    }

    public List<MaJiangGame> findLastGamesByUserAndDays(Integer userId, Integer days) {
        return majiangGameRepository.findLastGamesByUserAndDays(userId, days)
                .stream()
                .map(this::toMaJiangGame)
                .collect(Collectors.toList());
    }

    private MaJiangGame toMaJiangGame(MaJiangGameDO majiangGameDO) {
        if (majiangGameDO == null) {
            return null;
        }

        MaJiangGame majiangGame = new MaJiangGame();
        BeanUtils.copyProperties(majiangGameDO, majiangGame);
        majiangGame.setType(MaJiangGameType.fromCode(majiangGameDO.getType()));
        majiangGame.setDeleted(majiangGameDO.getIsDeleted() == 1);
        return majiangGame;
    }

    private MaJiangGameDO toMaJiangGameDO(MaJiangGame majiangGame) {
        if (majiangGame == null) {
            return null;
        }

        MaJiangGameDO majiangGameDO = new MaJiangGameDO();
        BeanUtils.copyProperties(majiangGame, majiangGameDO);
        majiangGameDO.setType(majiangGame.getType().getCode());
        majiangGameDO.setIsDeleted(majiangGame.isDeleted() ? 1 : 0);
        return majiangGameDO;
    }
}
