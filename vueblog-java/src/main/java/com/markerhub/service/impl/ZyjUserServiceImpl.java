package com.markerhub.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.ZyjUser;
import com.markerhub.mapper.ZyjUserMapper;
import com.markerhub.service.ZyjUserService;
import com.markerhub.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ZyjUserServiceImpl extends ServiceImpl<ZyjUserMapper, ZyjUser> implements ZyjUserService {
    @Autowired
    private ZyjUserMapper zyjUserMapper;

    @Override
    public Result addZyjUser(ZyjUser zyjUser) {
        ZyjUser user = new ZyjUser();
        user.setId(UUIDUtil.getUUID());
        user.setAccount(zyjUser.getAccount());
        user.setPassword(zyjUser.getPassword());
        user.setCookie("");
        user.setExpire("0");
        try{
            int i = zyjUserMapper.insert(user);
            if(i>0){
                return Result.succ("添加成功");
            }else{
                return Result.fail("添加失败");
            }
        }catch (Exception e){
            log.error("添加时出现异常----------->"+e.toString());
            return Result.fail("添加时出现异常"+e.toString());
        }
    }

    @Override
    public Result findZyjUsers() {
        List<ZyjUser> zyjUsers = null;
        try{
            zyjUsers = zyjUserMapper.queryZyjUsers();
            return Result.succ(zyjUsers);
        }catch (Exception e){
            log.error("查询时出现异常,异常原因:"+e.toString());
            return Result.fail("查询时出现异常,异常原因:"+e.toString());
        }
    }
}
