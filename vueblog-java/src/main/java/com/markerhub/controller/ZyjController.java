package com.markerhub.controller;

import com.alibaba.fastjson.JSONObject;
import com.markerhub.common.lang.Result;
import com.markerhub.constant.PcConstant;
import com.markerhub.entity.ZyjToken;
import com.markerhub.mapper.ZyjTokenMapper;
import com.markerhub.service.ZyjService;
import com.markerhub.util.TimeUtil;
import com.markerhub.vo.AddTokenVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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

import javax.servlet.http.HttpServletRequest;
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
        String url = "http://106.12.189.59/app/superscanPH/loginPH.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("m", "login");
        map.add("username", "13868612368");
        map.add("password", "gl112233");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        String s = response.getHeaders().get("Set-Cookie").get(0);
        System.out.println(response.getHeaders().get("Set-Cookie").get(0));
        System.out.println(response.getBody());
//        s.split(";")[0]+";"
        return  Result.succ(s.split(";")[0]+";");

    }

    @GetMapping(value = "/search")
    public Result zySearch(@RequestParam("searchName") String searchName,@RequestParam("code") String code,@RequestParam("id") String cookie){
        if(StringUtils.isEmpty(searchName)){
            return Result.fail("查询名称为空");
        }
        if(StringUtils.isEmpty(code)){
            return Result.fail("激活码为空");
        }
        if(StringUtils.isEmpty(cookie)){
            return Result.fail("cookie为空");
        }
        return zyjService.searchZyj(searchName,code,cookie);
    }

    @GetMapping(value = "/searchMarking")
    public Result getMarking(@RequestParam("searchName") String searchName,@RequestParam("code") String code,@RequestParam("id") String cookie){
        if(StringUtils.isEmpty(searchName)){
            return Result.fail("查询名称为空");
        }
        if(StringUtils.isEmpty(code)){
            return Result.fail("激活码为空");
        }
        if(StringUtils.isEmpty(cookie)){
            return Result.fail("cookie为空");
        }
        return zyjService.searchMarking(searchName,code,cookie);
    }


    @GetMapping(value = "/myRecord")
    public Result getMyRecord(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://139.159.141.200/app/superscanPH/opQuery.jsp";
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies =new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        cookies.add("UM_distinctid=1757d88999410f-02e5f5298d538d-c781f38-100200-1757d889995401; CNZZDATA4962612=cnzz_eid%3D1937187844-1606304311-http%253A%252F%252F139.159.141.200%252F%26ntime%3D1606304311; CNZZDATA1276815554=1646318526-1606304320-http%253A%252F%252F139.159.141.200%252F%7C1606304320; JSESSIONID=5E5F639D84A04222075D74BFCD4C9019; CNZZDATA1277894835=1541360565-1604126342-http%253A%252F%252F139.159.141.200%252F%7C1606476364");       //在 header 中存入cookies
        headers.put(HttpHeaders.COOKIE,cookies);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("m", "mine");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        return Result.succ(response.getBody());
    }
}
