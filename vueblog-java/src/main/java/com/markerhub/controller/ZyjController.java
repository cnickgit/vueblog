package com.markerhub.controller;

import com.alibaba.fastjson.JSONObject;
import com.markerhub.common.lang.Result;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@Slf4j
public class ZyjController {

    @Autowired
    private ZyjTokenMapper zyjTokenMapper;

    @Autowired
    private ZyjService zyjService;

    @GetMapping(value = "/zyjLogin")
    public Result zyjLog(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://124.71.87.53/app/superscanPH/loginPH.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("m", "login");
        map.add("username", "13868612368");
        map.add("password", "gl112233");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        String s = response.getHeaders().get("Set-Cookie").get(0);
        return  Result.succ(s.split(";")[0]+";");

    }

    @GetMapping(value = "/search")
    public Result zySearch(@RequestParam("searchName") String searchName,@RequestParam("code") String code){
        if(StringUtils.isEmpty(searchName)){
            return Result.fail("查询名称为空");
        }
        if(StringUtils.isEmpty(code)){
            return Result.fail("激活码为空");
        }
        return zyjService.searchZyj(searchName,code);
    }

    @GetMapping(value = "/searchMarking")
    public Result getMarking(@RequestParam("searchName") String searchName,@RequestParam("code") String code){
        if(StringUtils.isEmpty(searchName)){
            return Result.fail("查询名称为空");
        }
        if(StringUtils.isEmpty(code)){
            return Result.fail("激活码为空");
        }
        return zyjService.searchMarking(searchName,code);
    }

    @GetMapping(value = "/myRecord")
    public Result getMyRecord(@RequestParam("id") String cookie){
        return zyjService.getZyjToken(cookie);
    }

    @GetMapping(value = "/my")
    public Result zyjmy(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://106.12.189.59/app/superscanPH/opQuery.jsp";
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies =new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        //在 header 中存入cookies
        headers.put(HttpHeaders.COOKIE,cookies);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("m", "mine");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        String sycs = (String) jsonObject.get("qinf");
        return Result.succ(jsonObject);
    }


}
