package com.markerhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.ZyjUser;


public interface ZyjUserService extends IService<ZyjUser> {

    Result addZyjUser(ZyjUser zyjUser);

    Result findZyjUsers();

    Result findZyjUsersByPort(String port);
}
