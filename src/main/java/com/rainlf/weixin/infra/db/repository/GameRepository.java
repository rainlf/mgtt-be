package com.rainlf.weixin.infra.db.repository;

import com.rainlf.weixin.infra.db.model.Game;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author rain
 * @date 6/13/2024 10:34 PM
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {

    List<Game> findByIdIn(List<Integer> ids);
}
