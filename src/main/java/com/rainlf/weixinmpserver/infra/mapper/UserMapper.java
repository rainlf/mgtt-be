package com.rainlf.weixinmpserver.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rainlf.weixinmpserver.model.db.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where open_id = #{openId}")
    User selectByOpenId(@Param("openId") String openId);

    @Update("update user set asset = asset + #{assetChange} where id = #{id}")
    int updateAssetChange(@Param("id") Integer id, @Param("assetChange") Integer assetChange);
}




