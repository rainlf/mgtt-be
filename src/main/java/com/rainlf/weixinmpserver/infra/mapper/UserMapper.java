package com.rainlf.weixinmpserver.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rainlf.weixinmpserver.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
public interface UserMapper extends BaseMapper<User> {

    User selectByOpenId(@Param("openId") String openId);

    int updateAssetChange(@Param("id") Integer id, @Param("assetChange") Integer assetChange);
}




