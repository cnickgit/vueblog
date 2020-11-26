package com.markerhub.controller;

import com.alibaba.fastjson.JSONObject;
import com.markerhub.common.lang.Result;
import com.markerhub.constant.PcConstant;
import com.markerhub.entity.ZyjToken;
import com.markerhub.mapper.ZyjTokenMapper;
import com.markerhub.util.TimeUtil;
import com.markerhub.vo.AddTokenVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ZyjController {

    @Autowired
    private ZyjTokenMapper zyjTokenMapper;

    @GetMapping(value = "/addToken")
    public Result addZyjToken(@RequestParam("type") String type){
//        @RequestBody AddTokenVo addTokenVo
        ZyjToken zyjToken = new ZyjToken();
        zyjToken.setId(UUID.randomUUID().toString());
        zyjToken.setType(type);
        zyjToken.setCode(UUID.randomUUID().toString());
        zyjToken.setCreateTime(new Date());
        zyjToken.setUpdateTime(new Date());
        zyjToken.setEnable("0");
        if(PcConstant.TYPE_ONE.equals(type)){
            zyjToken.setPrescription(5);
            zyjToken.setMoney(1);
            zyjToken.setTypeRemarks("1元 5次 24小时有效");
        }else if(PcConstant.TYPE_THREE.equals(type)){
            zyjToken.setPrescription(30);
            zyjToken.setMoney(3);
            zyjToken.setTypeRemarks("3元 30次 24小时有效");
        }else if(PcConstant.TYPE_FIVE.equals(type)){
            zyjToken.setPrescription(60);
            zyjToken.setMoney(5);
            zyjToken.setTypeRemarks("5元 60次 24小时有效");
        }else if(PcConstant.TYPE_EVENGHT.equals(type)){
            zyjToken.setPrescription(50);
            zyjToken.setMoney(8);
            zyjToken.setTypeRemarks("8元 50次 不限制时间");
        }else if(PcConstant.TYPE_TYEFIVE.equals(type)){
            zyjToken.setPrescription(200);
            zyjToken.setMoney(25);
            zyjToken.setTypeRemarks("25元 200次 不限制时间");
        }
        int i = zyjTokenMapper.insert(zyjToken);
        if(i>0){
            return Result.succ("新增成功");
        }else{
            return Result.fail("新增失败");
        }
    }

    @GetMapping(value = "/enableToken")
    public Result enableZyjToken(@RequestParam("id") String id){
        ZyjToken zyjToken = zyjTokenMapper.selectById(id);
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

        }else if(PcConstant.TYPE_TYEFIVE.equals(zyjToken.getType())){
        }
        zyjTokenMapper.updateById(zyjToken);
        return null;
    }

    @GetMapping(value="/tokens")
    public Result getTokens(){
        List<ZyjToken> zyjTokens = zyjTokenMapper.queryAllTokens();
        return Result.succ(zyjTokens);
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
    public Result zySearch(@RequestParam("searchName") String searchName){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://139.159.141.200/app/superscanPH/opQuery.jsp";
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies =new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        cookies.add("UM_distinctid=1757d88999410f-02e5f5298d538d-c781f38-100200-1757d889995401; JSESSIONID=1C1ECA44F2C7976D500A6488A5B34CBB; CNZZDATA4962612=cnzz_eid%3D1937187844-1606304311-http%253A%252F%252F139.159.141.200%252F%26ntime%3D1606304311; CNZZDATA1276815554=1646318526-1606304320-http%253A%252F%252F139.159.141.200%252F%7C1606304320; CNZZDATA1277894835=1541360565-1604126342-http%253A%252F%252F139.159.141.200%252F%7C1606303281");       //在 header 中存入cookies
        headers.put(HttpHeaders.COOKIE,cookies);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("m", "queryAliim");
        map.add("aliim", searchName);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        System.out.println(response.getBody());
        return Result.succ(JSONObject.parseObject(response.getBody()));
    }
}
