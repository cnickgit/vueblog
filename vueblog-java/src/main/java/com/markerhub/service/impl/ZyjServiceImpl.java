package com.markerhub.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.common.lang.Result;
import com.markerhub.entity.Money;
import com.markerhub.entity.ZyjToken;
import com.markerhub.mapper.ZyjTokenMapper;
import com.markerhub.service.ZyjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ZyjServiceImpl  extends ServiceImpl<ZyjTokenMapper, ZyjToken> implements ZyjService{

    @Autowired
    private ZyjTokenMapper zyjTokenMapper;

    @Override
    public Result searchZyj(String searchName, String code, String cookie) {
        ZyjToken token = zyjTokenMapper.queryTokenByCode(code);
        if(token == null || token.getPrescription() < 1){
            return Result.fail("激活码失效");
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://106.12.189.59/app/superscanPH/opQuery.jsp";
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies =new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        //在 header 中存入cookies
        cookies.add(cookie);
        headers.put(HttpHeaders.COOKIE,cookies);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("m", "queryAliim");
        map.add("aliim", searchName);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        token.setPrescription(token.getPrescription() -1);
        zyjTokenMapper.updateById(token);
        System.out.println("body:"+response.getBody());
        Object parse = JSONObject.parse(response.getBody());
        return Result.succ(parse);
    }

    @Override
    public Result searchMarking(String searchName, String code, String cookie) {
        ZyjToken token = zyjTokenMapper.queryTokenByCode(code);
        if(token == null || token.getPrescription() < 1){
            return Result.fail("激活码失效");
        }
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://106.12.189.59/app/superscanPH/opQuery.jsp";
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies =new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        //在 header 中存入cookies
        cookies.add(cookie);
        headers.put(HttpHeaders.COOKIE,cookies);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("m", "getQWRs");
        map.add("wxorqq", searchName);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        token.setPrescription(token.getPrescription() -1);
        zyjTokenMapper.updateById(token);
        System.out.println("body:"+response.getBody());
        Object parse = JSONObject.parse(response.getBody());
        return Result.succ(parse);
    }

    @Override
    public Result getMoney() {
        Money money = zyjTokenMapper.getMoney();
        return Result.succ(money);
    }
}
