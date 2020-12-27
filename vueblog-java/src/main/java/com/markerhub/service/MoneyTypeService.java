package com.markerhub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.MoneyType;
import com.markerhub.vo.AddMoneyTypeVo;

public interface MoneyTypeService extends IService<MoneyType> {
    public Result addMoneyType(AddMoneyTypeVo addMoneyTypeVo);

    public Result getMoneyType();

    public Result getMoneyTypeById(String id);

    public Result removeMoneyTypeById(String id);
}
