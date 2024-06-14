package com.rainlf.weixin.infra.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rainlf.weixin.infra.db.model.UserAsset;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author rain
 * @date 6/13/2024 10:34 PM
 */
@Mapper
public interface UserAssetMapper extends BaseMapper<UserAsset> {
}
