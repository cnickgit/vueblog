package com.markerhub.config;

import com.markerhub.constant.PcConstant;
import com.markerhub.entity.ZyjToken;
import com.markerhub.entity.ZyjUser;
import com.markerhub.mapper.ZyjTokenMapper;
import com.markerhub.mapper.ZyjUserMapper;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.expr.instruct.ForEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: vueblog
 * @description:
 * @author: wsg
 * @create: 2020-11-26 11:20
 **/
@Component
@Slf4j
public class ScheduledTask {
    @Autowired
    private ZyjTokenMapper zyjTokenMapper;

    @Autowired
    private ZyjUserMapper zyjUserMapper;

    @Scheduled(cron = "0 0/5 * * * *")
    public void work(){
        List<ZyjToken> zyjTokens = zyjTokenMapper.queryEnableToken();
        Calendar cal = Calendar.getInstance();
        for(ZyjToken token : zyjTokens){
            System.out.println("time:"+token.getEndTime());
            if(null != token.getEndTime()){
                if(cal.getTime().compareTo(token.getEndTime()) > 0 || token.getRemainingTimes() < 1){
                    System.out.println("token过期了");
                    //已过期
                    token.setEnable("2");
                    zyjTokenMapper.updateById(token);
                }
            }
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.error("每隔五分钟执行一次,执行时间:"+df.format(new Date()));
    }

    @Scheduled(cron = "0 5 0 * * ?")
    public void clearUp(){
        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.error("每天凌晨12点05执行一次,执行时间:"+df.format(new Date()));
        List<ZyjUser> zyjUsers = zyjUserMapper.queryZyjUsers();
        List<String> ids = new ArrayList<>();
        for (ZyjUser user:zyjUsers) {
            user.setQueryNum(0);
            ids.add(user.getAccount());
        }
        if(!CollectionUtils.isEmpty(ids)){
            zyjUserMapper.updateBatchByUserIds(ids);
        }
    }
}
