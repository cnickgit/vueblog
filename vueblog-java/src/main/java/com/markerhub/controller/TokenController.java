package com.markerhub.controller;

import com.alibaba.fastjson.JSONObject;
import com.markerhub.common.lang.Result;
import com.markerhub.constant.PcConstant;
import com.markerhub.entity.MoneyType;
import com.markerhub.entity.ZyjToken;
import com.markerhub.mapper.MoneyTypeMapper;
import com.markerhub.mapper.ZyjTokenMapper;
import com.markerhub.service.ZyjService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@Slf4j
public class TokenController {
    @Autowired
    private ZyjTokenMapper zyjTokenMapper;

    @Autowired
    private MoneyTypeMapper moneyTypeMapper;

    @Autowired
    private ZyjService zyjService;

    @GetMapping(value = "/addToken")
    public Result addZyjToken(@RequestParam("typeId") String typeId, @RequestParam("num") Integer num){
        MoneyType moneyType = moneyTypeMapper.selectById(typeId);
        List<ZyjToken> zyjTokens = new ArrayList<>();
        for(int i=0;i<num;i++){
            ZyjToken zyjToken = new ZyjToken();
            zyjToken.setId(UUID.randomUUID().toString());
            zyjToken.setTypeId(typeId);
            zyjToken.setCode(UUID.randomUUID().toString());
            zyjToken.setCreateTime(new Date());
            zyjToken.setUpdateTime(new Date());
            zyjToken.setEnable("0");
            zyjToken.setExportStatus("0");
            zyjToken.setMoney(moneyType.getMoney());
            zyjToken.setPrescription(moneyType.getQueryNum());
            zyjToken.setRemainingTimes(moneyType.getQueryNum());
            zyjToken.setTypeRemarks(moneyType.getRemarks());
            zyjToken.setLimitTime(moneyType.getTimeType());
            zyjTokens.add(zyjToken);
        }
        int i = zyjTokenMapper.insertBatch(zyjTokens);
        if(i>0){
            return Result.succ("新增成功");
        }else{
            return Result.fail("新增失败");
        }
    }

    @GetMapping(value = "/enableToken")
    public Result enableZyjToken(@RequestParam("code") String code){
        ZyjToken zyjToken = zyjTokenMapper.queryToken(code);
        if(null == zyjToken){
            return Result.fail("激活码无效");
        }
        if(PcConstant.ENABLE_NO.equals(zyjToken.getEnable())){
            zyjToken.setEnable("1");
            Calendar cal = Calendar.getInstance();
            zyjToken.setEnableTime(cal.getTime());
            cal.add(Calendar.DATE, 1);//增加一天
            zyjToken.setEndTime(cal.getTime());
        }else if(PcConstant.ENABLE_YES.equals(zyjToken.getEnable())){
            return Result.succ("启用成功");
        }else if(PcConstant.ENABLE_EXPIRE.equals(zyjToken.getEnable())){
            return Result.succ("您的激活码已过期");
        }
        int i=0;
        try {
            i = zyjTokenMapper.updateById(zyjToken);
            if(i>0){
                return Result.succ("token启动成功!");
            }else{
                return Result.succ("token启动失败!");
            }
        }catch (Exception e){
            log.error("token启动失败,失败原因--->",e.toString());
            return Result.succ("token启动失败,失败原因--->"+e.toString());
        }
    }

    @GetMapping(value="/tokens")
    public Result getTokens(@RequestParam("enableType") String enableType){
        List<ZyjToken> zyjTokens = null;
        try {
            zyjTokens = zyjTokenMapper.queryAllTokens(enableType);
            return Result.succ(zyjTokens);
        }catch (Exception e){
            log.error("查询失败,失败原因:--->",e.toString());
            return Result.fail("查询失败,失败原因:--->"+e.toString());
        }

    }

    @GetMapping(value="/exportExcelAll")
    public Result exportExcel(HttpServletRequest request, HttpServletResponse response,@RequestParam("typeId") String typeId) {
        return zyjService.exportAllExcel(request,response,typeId);
    }

    @GetMapping(value="/money")
    public Result getCurrentMonthMony(){
        return zyjService.getMoney();
    }

    @GetMapping(value = "zyjMy")
    public Result getZyjMy(){
        return zyjService.getZyjMy();
    }
}
