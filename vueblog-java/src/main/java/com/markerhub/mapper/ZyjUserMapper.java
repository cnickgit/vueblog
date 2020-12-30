package com.markerhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.entity.ZyjUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZyjUserMapper extends BaseMapper<ZyjUser> {
    List<ZyjUser>  queryZyjUsers();

    Integer updateBatchByIds(@Param("id") String id);
}
