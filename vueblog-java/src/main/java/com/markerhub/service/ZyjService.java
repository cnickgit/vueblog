package com.markerhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.ZyjToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ZyjService extends IService<ZyjToken> {

    Result searchZyj(String searchName,String code,String cookie);

    Result searchMarking(String searchName,String code,String cookie);

    Result getMoney();

    Result getZyjToken(String id);

    Result getZyjMy();

    Result exportAllExcel(HttpServletRequest request, HttpServletResponse response,Integer num);

}
