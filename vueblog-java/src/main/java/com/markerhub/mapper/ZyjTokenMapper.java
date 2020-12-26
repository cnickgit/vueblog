package com.markerhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.markerhub.entity.Money;
import com.markerhub.entity.TokenExcel;
import com.markerhub.entity.ZyjToken;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ZyjTokenMapper extends BaseMapper<ZyjToken> {
    public List<ZyjToken> queryEnableToken();

    public List<ZyjToken> queryAllTokens(@Param("enableType") String enableType);

    public List<TokenExcel> queryNotEnableTokens(@Param("typeId") String typeId);

    public ZyjToken queryTokenByCode(@Param("code") String code);

    public ZyjToken queryToken(@Param("code") String code);

    Integer insertBatch(@Param("tokens") List<ZyjToken> zyjTokens);

    Money getMoney();

    Integer updateBatchExportStatus(@Param("codes") List<String> codes);




}
