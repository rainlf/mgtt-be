package com.rainlf.weixin.infra.db.repository;

import com.rainlf.weixin.domain.consts.GameDetailTypeEnum;
import com.rainlf.weixin.infra.db.model.GameDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rain
 * @date 6/13/2024 10:34 PM
 */
@Repository
public interface GameDetailRepository extends JpaRepository<GameDetail, Integer> {

    List<GameDetail> findByUserIdAndType(Integer userId, Integer type);

    List<GameDetail> findByUserIdAndType(Integer userId, Integer type, Pageable pageable);

    List<GameDetail> findByGameIdIn(List<Integer> gameIds);
}
