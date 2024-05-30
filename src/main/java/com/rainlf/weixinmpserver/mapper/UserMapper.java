package com.rainlf.weixinmpserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rainlf.weixinmpserver.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author rain
 * @date 5/21/2024 7:29 AM
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
