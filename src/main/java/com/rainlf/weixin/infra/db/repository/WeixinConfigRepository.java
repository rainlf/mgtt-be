package com.rainlf.weixin.infra.db.repository;

import com.rainlf.weixin.infra.db.model.WeixinConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author rain
 * @date 6/23/2024 8:19 PM
 */
@Repository
public interface WeixinConfigRepository extends JpaRepository<WeixinConfig, Integer> {
}
