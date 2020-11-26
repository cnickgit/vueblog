package com.markerhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.entity.ZyjToken;

import java.util.List;

public interface ZyjTokenMapper extends BaseMapper<ZyjToken> {
    public List<ZyjToken> queryEnableToken();

    public List<ZyjToken> queryAllTokens();
}
