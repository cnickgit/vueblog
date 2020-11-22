package com.markerhub.controller;

import com.markerhub.common.lang.Result;
import com.markerhub.entity.ZyjToken;
import com.markerhub.vo.AddTokenVo;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
public class ZyjController {

    @PostMapping(value = "/addToken")
    public Result addZyjToken(@RequestBody AddTokenVo addTokenVo){
        ZyjToken zyjToken = new ZyjToken();
        zyjToken.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(addTokenVo,zyjToken);
        return null;
    }


    @GetMapping("checkJWT")
    public Result checkJwt(String code){



        return null;
    }

    @GetMapping(value = "/zyjLogin")
    public String zyjLog(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://139.159.141.200/app/superscan/op.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("m", "login");
        map.add("username", "13868612368");
        map.add("password", "gl112233");
        map.add("parcame", "ajax");
        map.add("type", "1");
        map.add("origin", "cktoolApp");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        System.out.println(response.getHeaders().get("Set-Cookie").get(0));
        System.out.println(response.getBody());
        return response.getBody();
    }

    @GetMapping(value = "/search")
    public String zySearch(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://139.159.141.200/app/superscanPH/opQuery.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("m", "sAliim");
        map.add("ifQBase", "true");
        map.add("ifQReport", "true");
        map.add("judgeAnother", "true");
        map.add("aliim", "aa");
        map.add("c", "nznd");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        System.out.println(response.getHeaders().get("Set-Cookie").get(0));
        System.out.println(response.getBody());
        return response.getBody();
    }
}
