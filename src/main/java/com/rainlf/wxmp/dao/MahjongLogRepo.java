package com.rainlf.wxmp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rain
 * @date 4/20/2024 9:19 PM
 */
@Repository
public interface MahjongLogRepo extends JpaRepository<MahjongLog, Long> {
}
