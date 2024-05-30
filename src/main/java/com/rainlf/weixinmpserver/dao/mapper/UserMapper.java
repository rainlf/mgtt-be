package com.rainlf.weixinmpserver.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rainlf.weixinmpserver.dao.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * @author rain
 * @date 5/21/2024 7:28 AM
 */
public interface UserMapper extends BaseMapper<User> {

    User selectByOpenId(@Param("openId") String openId);

}




