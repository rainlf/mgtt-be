package com.rainlf.weixin.infra.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rainlf.weixin.infra.db.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
 * @author rain
 * @date 6/13/2024 10:34 PM
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    Optional<User> findByOpenId(String openId);
}
