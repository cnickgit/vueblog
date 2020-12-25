package com.markerhub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.MoneyType;
import com.markerhub.mapper.MoneyTypeMapper;
import com.markerhub.service.MoneyTypeService;
import com.markerhub.util.TimeUtil;
import com.markerhub.util.UUIDUtil;
import com.markerhub.vo.AddMoneyTypeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class MoneyTypeServiceImpl extends ServiceImpl<MoneyTypeMapper, MoneyType> implements MoneyTypeService {

   @Autowired
   private MoneyTypeMapper moneyTypeMapper;

    @Override
    public Result addMoneyType(AddMoneyTypeVo addMoneyTypeVo) {
        try{
            MoneyType moneyType = new MoneyType();
            int i = 0;
            if(!StringUtils.isEmpty(addMoneyTypeVo.getId())){
                moneyType.setId(addMoneyTypeVo.getId());
                moneyType.setMoney(addMoneyTypeVo.getMoney());
                moneyType.setTimeType(addMoneyTypeVo.getTimeType());
                moneyType.setQueryNum(addMoneyTypeVo.getQueryNum());
                moneyType.setUpdateTime(TimeUtil.getCurrentDate());
                i = moneyTypeMapper.updateById(moneyType);
                return Result.succ("修改成功");
            }else{
                moneyType.setId(UUIDUtil.getUUID());
                moneyType.setMoney(addMoneyTypeVo.getMoney());
                moneyType.setTimeType(addMoneyTypeVo.getTimeType());
                moneyType.setQueryNum(addMoneyTypeVo.getQueryNum());
                moneyType.setCreateTime(TimeUtil.getCurrentDate());
                moneyType.setUpdateTime(TimeUtil.getCurrentDate());
                i = moneyTypeMapper.insert(moneyType);
                return Result.succ("新增成功");
            }
        }catch (Exception e){
            log.error("新增或修改事件类型是出现异常,异常原因:"+e.toString());
            return Result.fail("新增或修改事件类型是出现异常,异常原因:"+e.toString());
        }
    }

    @Override
    public Result getMoneyType() {
        List<MoneyType> moneyTypes = null;
        try{
            moneyTypes = moneyTypeMapper.getMoneyType();
            return Result.succ(moneyTypes);
        }catch (Exception e){
            log.error("获取金钱类型出现异常,异常原因:"+e.toString());
            return Result.fail("获取金钱类型出现异常,异常原因:"+e.toString());
        }
    }
}
