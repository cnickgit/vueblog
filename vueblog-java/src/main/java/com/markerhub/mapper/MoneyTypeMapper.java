package com.markerhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.entity.MoneyType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MoneyTypeMapper extends BaseMapper<MoneyType> {
    public List<MoneyType> getMoneyType();

    public Integer removeById(@Param("id") String id);
}
