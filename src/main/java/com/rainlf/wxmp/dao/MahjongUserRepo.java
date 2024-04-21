package com.rainlf.wxmp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rain
 * @date 4/20/2024 9:20 PM
 */
@Repository
public interface MahjongUserRepo extends JpaRepository<MahjongUser, Long> {

    MahjongUser findByName(String name);

}
