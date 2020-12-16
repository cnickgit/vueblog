package com.markerhub.controller;

import com.alibaba.fastjson.JSONObject;
import com.markerhub.common.lang.Result;
import com.markerhub.constant.PcConstant;
import com.markerhub.entity.ZyjToken;
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
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@Slf4j
public class TokenController {
    @Autowired
    private ZyjTokenMapper zyjTokenMapper;

    @Autowired
    private ZyjService zyjService;

    @GetMapping(value = "/addToken")
    public Result addZyjToken(@RequestParam("type") String type, @RequestParam("num") Integer num){
//        @RequestBody AddTokenVo addTokenVo
        List<ZyjToken> zyjTokens = new ArrayList<>();
        for(int i=0;i<num;i++){
            ZyjToken zyjToken = new ZyjToken();
            zyjToken.setId(UUID.randomUUID().toString());
            zyjToken.setType(type);
            zyjToken.setCode(UUID.randomUUID().toString());
            zyjToken.setCreateTime(new Date());
            zyjToken.setUpdateTime(new Date());
            zyjToken.setEnable("0");
            zyjToken.setExportStatus("0");
            if(PcConstant.TYPE_ONE.equals(type)){
                zyjToken.setPrescription(5);
                zyjToken.setRemainingTimes(5);
                zyjToken.setMoney(1);
                zyjToken.setTypeRemarks("1元 5次 24小时有效");
            }else if(PcConstant.TYPE_THREE.equals(type)){
                zyjToken.setPrescription(30);
                zyjToken.setRemainingTimes(30);
                zyjToken.setMoney(3);
                zyjToken.setTypeRemarks("3元 30次 24小时有效");
            }else if(PcConstant.TYPE_FIVE.equals(type)){
                zyjToken.setPrescription(60);
                zyjToken.setRemainingTimes(60);
                zyjToken.setMoney(5);
                zyjToken.setTypeRemarks("5元 60次 24小时有效");
            }else if(PcConstant.TYPE_EVENGHT.equals(type)){
                zyjToken.setPrescription(50);
                zyjToken.setRemainingTimes(50);
                zyjToken.setMoney(8);
                zyjToken.setTypeRemarks("8元 50次 不限制时间");
            }else if(PcConstant.TYPE_TYEFIVE.equals(type)){
                zyjToken.setPrescription(200);
                zyjToken.setRemainingTimes(200);
                zyjToken.setMoney(25);
                zyjToken.setTypeRemarks("25元 200次 不限制时间");
            }
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
            if(PcConstant.TYPE_ONE.equals(zyjToken.getType())){
                Calendar cal = Calendar.getInstance();
                zyjToken.setEnableTime(cal.getTime());
                cal.add(Calendar.DATE, 1);//增加一天
                zyjToken.setEndTime(cal.getTime());
                zyjToken.setEnable("1");
            }else if(PcConstant.TYPE_THREE.equals(zyjToken.getType())){
                Calendar cal = Calendar.getInstance();
                zyjToken.setEnableTime(cal.getTime());
                cal.add(Calendar.DATE, 1);//增加一天
                zyjToken.setEndTime(cal.getTime());
                zyjToken.setEnable("1");
            }else if(PcConstant.TYPE_FIVE.equals(zyjToken.getType())){
                Calendar cal = Calendar.getInstance();
                zyjToken.setEnableTime(cal.getTime());
                cal.add(Calendar.DATE, 1);//增加一天
                zyjToken.setEndTime(cal.getTime());
                zyjToken.setEnable("1");
            }else if(PcConstant.TYPE_EVENGHT.equals(zyjToken.getType())){
                zyjToken.setEnable("1");
            }else if(PcConstant.TYPE_TYEFIVE.equals(zyjToken.getType())){
                zyjToken.setEnable("1");
            }
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
    public void exportExcel(HttpServletRequest request, HttpServletResponse response,@RequestParam("num") Integer num) {
        zyjService.exportAllExcel(request,response,num);
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
