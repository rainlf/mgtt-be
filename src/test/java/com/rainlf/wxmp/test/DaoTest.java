package com.rainlf.wxmp.test;

import com.rainlf.wxmp.dao.MahjongLog;
import com.rainlf.wxmp.dao.MahjongLogRepo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author rain
 * @date 4/21/2024 7:33 AM
 */
@Slf4j
@SpringBootTest
public class DaoTest {

    @Autowired
    private MahjongLogRepo mahjongLogRepo;

    @Test
    public void insertTest() {
        MahjongLog mahjongLog = new MahjongLog();
        mahjongLog.setMessage("123");
        mahjongLog = mahjongLogRepo.save(mahjongLog);
        log.info("mahjongLog: {}", mahjongLog);
    }

    @Test
    public void updateTest() {
        MahjongLog mahjongLog = mahjongLogRepo.findById(1L).orElse(null);
        log.info("mahjongLog: {}", mahjongLog);
        if (mahjongLog != null) {
            mahjongLog.setMessage("88");
            mahjongLog = mahjongLogRepo.save(mahjongLog);
        }
        log.info("mahjongLog: {}", mahjongLog);
    }
}
