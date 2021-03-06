package com.markerhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.entity.ZyjUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZyjUserMapper extends BaseMapper<ZyjUser> {

    List<ZyjUser>  queryZyjUsers();

    List<ZyjUser>  queryAllZyjUsers();

    List<ZyjUser>  queryZyjUsersByPort(String port);

    Integer updateBatchByIds(@Param("id") String id,@Param("port") String port);

    Integer updateBatchByUserIds(@Param("ids") List<String> ids);

    Integer updateUserByAccount(@Param("account") String account,@Param("num") int num);

    Integer updateUserStatusByAccount(@Param("account") String account);
}
