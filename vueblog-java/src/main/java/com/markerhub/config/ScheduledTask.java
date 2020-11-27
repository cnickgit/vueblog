package com.markerhub.config;

import com.markerhub.constant.PcConstant;
import com.markerhub.entity.ZyjToken;
import com.markerhub.mapper.ZyjTokenMapper;
import net.sf.saxon.expr.instruct.ForEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

/**
 * @program: vueblog
 * @description:
 * @author: wsg
 * @create: 2020-11-26 11:20
 **/
@Component
public class ScheduledTask {
    @Autowired
    private ZyjTokenMapper zyjTokenMapper;

    @Scheduled(cron = "0 0/5 * * * *")
    public void work(){
        List<ZyjToken> zyjTokens = zyjTokenMapper.queryEnableToken();
        Calendar cal = Calendar.getInstance();
        for(ZyjToken token : zyjTokens){
            System.out.println("time:"+token.getEndTime());
            if(cal.getTime().compareTo(token.getEndTime()) > 0 ){
                System.out.println("token过期了");
                //已过期
                token.setEnable("2");
                zyjTokenMapper.updateById(token);
            }
        }
        System.out.print("执行一次\n");
    }
}
