package com.markerhub.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.common.lang.Result;
import com.markerhub.constant.PcConstant;
import com.markerhub.entity.Money;
import com.markerhub.entity.TokenExcel;
import com.markerhub.entity.ZyjToken;
import com.markerhub.entity.ZyjUser;
import com.markerhub.mapper.ZyjTokenMapper;
import com.markerhub.mapper.ZyjUserMapper;
import com.markerhub.service.ZyjService;
import com.markerhub.service.ZyjUserService;
import com.markerhub.util.FileDownloadUtil;
import com.markerhub.vo.TokenDto;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Transient;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class ZyjServiceImpl  extends ServiceImpl<ZyjTokenMapper, ZyjToken> implements ZyjService{

    @Autowired
    private ZyjTokenMapper zyjTokenMapper;
    @Autowired
    private ZyjUserMapper zyjUserMapper;

    public void setCookies(ZyjUser user){
        //cookies为空或者已过期
        if(StringUtils.isEmpty(user.getCookie()) || user.getExpire().equals("1")){
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://106.12.189.59/app/superscanPH/loginPH.jsp";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("m", "login");
            map.add("username", user.getAccount());
            map.add("password", user.getPassword());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
            String s = response.getHeaders().get("Set-Cookie").get(0);
            user.setCookie(s.split(";")[0]+";");
            user.setExpire("0");
            zyjUserMapper.updateById(user);
        }
    }

    public void setQuerySequence(ZyjUser user){
            //获取查询次数
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://106.12.189.59/app/superscanPH/opQuery.jsp";
            HttpHeaders headers = new HttpHeaders();
            List<String> cookies =new ArrayList<String>();
            /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
            //在 header 中存入cookies
            cookies.add(user.getCookie());
            headers.put(HttpHeaders.COOKIE,cookies);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("m", "mine");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
            if("ns".equals(jsonObject.get("result"))){
                //cookies过期
                this.setCookies(user);
                cookies.add(user.getCookie());
                headers.put(HttpHeaders.COOKIE,cookies);
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                map.add("m", "mine");
                response = restTemplate.postForEntity( url, request , String.class );
                jsonObject = JSONObject.parseObject(response.getBody());
                String sycs = (String) jsonObject.get("qinf");
                String leaveNum = sycs.split("/")[1];
                user.setLeaveNum(leaveNum);
            }else{
                String sycs = (String) jsonObject.get("qinf");
                String leaveNum = sycs.split("/")[1];
                user.setLeaveNum(leaveNum);
            }
    }

    public boolean isNext(ZyjUser user){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://106.12.189.59/app/superscanPH/opQuery.jsp";
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies =new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        //在 header 中存入cookies
        cookies.add(user.getCookie());
        headers.put(HttpHeaders.COOKIE,cookies);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("m", "mine");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        if("ns".equals(jsonObject.get("result"))){
            //cookies过期
            cookies.clear();
            user.setExpire("1");
            this.setCookies(user);
            cookies.add(user.getCookie());
            headers.put(HttpHeaders.COOKIE,cookies);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            map.add("m", "mine");
            response = restTemplate.postForEntity( url, request , String.class );
            jsonObject = JSONObject.parseObject(response.getBody());
            String sycs = (String) jsonObject.get("qinf");
            String leaveNum = sycs.split("/")[1];
            String substring = leaveNum.substring(2);
            String day = substring.substring(0, substring.indexOf("次"));
            int num = Integer.parseInt(day);
            user.setLeaveNum(leaveNum);
            if(user.getMaxTimes() < num){
                user.setUseStatus("使用中");
                return true;
            }else{
                user.setUseStatus("停用");
                zyjUserMapper.updateById(user);
                return false;
            }
        }else{
            String sycs = (String) jsonObject.get("qinf");
            String leaveNum = sycs.split("/")[1];
            user.setLeaveNum(leaveNum);
            String substring = leaveNum.substring(2);
            String day = substring.substring(0, substring.indexOf("次"));
            int num = Integer.parseInt(day);
            user.setLeaveNum(leaveNum);
            if(user.getMaxTimes() < num){
                user.setUseStatus("使用中");
                return true;
            }else{
                user.setUseStatus("停用");
                zyjUserMapper.updateById(user);
                return false;
            }
        }
    }

    @Override
    public Result searchZyj(String searchName, String code) {
        ZyjToken token = zyjTokenMapper.queryTokenByCode(code);
        if(token == null){
            return Result.fail("激活码失效");
        }
        if(token.getRemainingTimes() < 1){
            return Result.fail("对不起，您的查询次数已用完");
        }
        if("2".equals(token.getEnable())){
            return Result.fail("对不起，您的激活码已到期");
        }
        List<ZyjUser> zyjUsers = zyjUserMapper.queryZyjUsers();
        //查询
        JSONObject jsonObject = null;
        for(ZyjUser user : zyjUsers){
            if(!this.isNext(user)){
                continue;
            }
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://106.12.189.59/app/superscanPH/opQuery.jsp";
            HttpHeaders headers = new HttpHeaders();
            List<String> cookies =new ArrayList<String>();
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            cookies.add(user.getCookie());
            headers.put(HttpHeaders.COOKIE,cookies);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            map.add("m", "queryAliim");
            map.add("aliim", searchName);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
            jsonObject = JSONObject.parseObject(response.getBody());
            String result = jsonObject.get("result").toString();
            if("ns".equals(result)){
                //cookies过期
                user.setExpire("1");
                this.setCookies(user);
                /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
                //在 header 中存入cookies
                cookies.add(user.getCookie());
                headers.put(HttpHeaders.COOKIE,cookies);
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                map.add("m", "queryAliim");
                map.add("aliim", searchName);
                request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                response = restTemplate.postForEntity( url, request , String.class );
                jsonObject = JSONObject.parseObject(response.getBody());
                //需要验证码
                if("y".equals(jsonObject.get("iff"))){
                    continue;
                }
            }else if("账号不存在".equals(result)){
                break;
            }else {
                token.setRemainingTimes(token.getRemainingTimes() -1);
                zyjTokenMapper.updateById(token);
                this.setQuerySequence(user);
                zyjUserMapper.updateById(user);
                zyjUserMapper.updateBatchByIds(user.getId());
                break;
            }
        }
        return Result.succ(jsonObject);
    }

    @Override
    public Result searchMarking(String searchName, String code) {
        ZyjToken token = zyjTokenMapper.queryTokenByCode(code);
        if(token == null || token.getPrescription() < 1){
            return Result.fail("激活码失效");
        }
        //调用登录接口获取cookie查询次数
        List<ZyjUser> zyjUsers = zyjUserMapper.queryZyjUsers();
        JSONObject jsonObject = null;
        for(ZyjUser user : zyjUsers){
            if(!this.isNext(user)){
                continue;
            }
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://106.12.189.59/app/superscanPH/opQuery.jsp";
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            List<String> cookies =new ArrayList<String>();
            /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
            //在 header 中存入cookies
            cookies.add(user.getCookie());
            headers.put(HttpHeaders.COOKIE,cookies);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            map.add("m", "getQWRs");
            map.add("wxorqq", searchName);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
            jsonObject = JSONObject.parseObject(response.getBody());
            String result = jsonObject.get("result").toString();
            if("ns".equals(result)){
                //token过期
                //cookies过期
                cookies.clear();
                user.setExpire("1");
                this.setCookies(user);
                cookies.add(user.getCookie());
                headers.put(HttpHeaders.COOKIE,cookies);
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                map.add("m", "getQWRs");
                map.add("wxorqq", searchName);
                jsonObject = JSONObject.parseObject(response.getBody());
                //需要验证码
                if("y".equals(jsonObject.get("iff"))){
                    continue;
                }
            }else if("wxnodata".equals(result)){
                break;
            }else{
                token.setRemainingTimes(token.getRemainingTimes() -1);
                zyjTokenMapper.updateById(token);
                this.setQuerySequence(user);
                zyjUserMapper.updateById(user);
                break;
            }
        }
        return Result.succ(jsonObject);
    }

    @Override
    public Result getMoney() {
        Money money = zyjTokenMapper.getMoney();
        return Result.succ(money);
    }

    @Override
    public Result getZyjToken(String id) {
        ZyjToken zyjToken = null;
        try {
            zyjToken = zyjTokenMapper.queryToken(id);
            return Result.succ(zyjToken);
        }catch (Exception e){
            log.error("查询token出现异常:"+e.toString());
            return Result.fail("查询token出现异常:"+e.toString());
        }
    }

    public List<TokenDto> setCookie(List<ZyjUser> zyjUsers){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://106.12.189.59/app/superscanPH/loginPH.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List<TokenDto> tokenDtos = new ArrayList<>();
        for (ZyjUser zyjUser:zyjUsers) {
            TokenDto tokenDto = new TokenDto();
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("m", "login");
            map.add("username", zyjUser.getAccount());
            map.add("password", zyjUser.getPassword());
            tokenDto.setAccount(zyjUser.getAccount());
            tokenDto.setPassword(zyjUser.getPassword());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
            String s = response.getHeaders().get("Set-Cookie").get(0);
            tokenDto.setCookie(s.split(";")[0]+";");
            this.setMy(tokenDto);
            //再查我的
            tokenDtos.add(tokenDto);
        }
        return tokenDtos;
    }

    public void  setMy(TokenDto tokenDto){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://106.12.189.59/app/superscanPH/opQuery.jsp";
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies =new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        //在 header 中存入cookies
        cookies.add(tokenDto.getCookie());
        headers.put(HttpHeaders.COOKIE,cookies);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("m", "mine");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
        Object parse = JSONObject.parse(response.getBody());
        tokenDto.setObj(parse);
    }

    @Override
    public Result getZyjMy() {
        //查询所有的账号
        List<ZyjUser> zyjUsers = zyjUserMapper.queryZyjUsers();
        if(CollectionUtils.isEmpty(zyjUsers)){
            return null;
        }
        //设置cookies
        List<TokenDto> tokenDtos = this.setCookie(zyjUsers);
        return Result.succ(tokenDtos);
    }

    @Override
    public Result exportAllExcel(HttpServletRequest request, HttpServletResponse response,String typeId) {
        try {
            //查询
            List<TokenExcel> zyjTokens = zyjTokenMapper.queryNotEnableTokens(typeId);
            List<String> codes = new ArrayList<>();
            for(TokenExcel token : zyjTokens){
                String prifx = "http://182.92.126.206:8081/";
                codes.add(token.getCode());
                token.setCode(prifx+token.getCode());
            }
            if(!CollectionUtils.isEmpty(codes)){
                zyjTokenMapper.updateBatchExportStatus(codes);
            }
            return Result.succ(FileDownloadUtil.export(response, zyjTokens,TokenExcel.class));
        }catch (Exception e){
            log.error("导出数据出现异常，异常原因:"+e.toString());
            return Result.fail("导出数据出现异常，异常原因:"+e.toString());
        }

    }
}
