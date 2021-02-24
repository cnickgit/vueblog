package com.markerhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.vo.LoginUser;
import org.apache.ibatis.annotations.Param;

/**
 * @program: vueblog
 * @description:
 * @author: wsg
 * @create: 2021-02-24 16:23
 **/
public interface LoginUserMapper extends BaseMapper<LoginUser> {
    public int selectUser(@Param("userName") String userName, @Param("password") String passWord);
}
