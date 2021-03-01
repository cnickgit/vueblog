package com.markerhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.entity.HhUser;

public interface HhUserMapper extends BaseMapper<HhUser> {
    HhUser selectTopUser();
}
