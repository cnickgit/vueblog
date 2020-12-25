package com.markerhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.entity.MoneyType;

import java.util.List;

public interface MoneyTypeMapper extends BaseMapper<MoneyType> {
    public List<MoneyType> getMoneyType();
}
