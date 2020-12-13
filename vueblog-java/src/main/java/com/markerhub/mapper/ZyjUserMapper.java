package com.markerhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.entity.ZyjUser;

import java.util.List;

public interface ZyjUserMapper extends BaseMapper<ZyjUser> {
    List<ZyjUser>  queryZyjUsers();
}
