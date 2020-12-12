package com.markerhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.ZyjToken;

public interface ZyjService extends IService<ZyjToken> {

    Result searchZyj(String searchName,String code,String cookie);

    Result searchMarking(String searchName,String code,String cookie);

    Result getMoney();

}