package com.markerhub.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.markerhub.common.lang.Result;
import com.markerhub.constant.PcConstant;
import com.markerhub.entity.*;
import com.markerhub.mapper.HhUserMapper;
import com.markerhub.mapper.ZyjTokenMapper;
import com.markerhub.mapper.ZyjUserMapper;
import com.markerhub.service.ZyjService;
import com.markerhub.util.FileDownloadUtil;
import com.markerhub.util.UUIDUtil;
import com.markerhub.vo.TokenDto;
import com.sun.imageio.plugins.common.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.*;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Service
public class ZyjServiceImpl extends ServiceImpl<ZyjTokenMapper, ZyjToken> implements ZyjService {


    static BASE64Encoder encoder = new sun.misc.BASE64Encoder();

    @Autowired
    private ZyjTokenMapper zyjTokenMapper;
    @Autowired
    private ZyjUserMapper zyjUserMapper;
    @Autowired
    private HhUserMapper hhUserMapper;

    public boolean setCookies(ZyjUser user) {
        //cookies为空或者已过期
        if (StringUtils.isEmpty(user.getCookie()) || user.getExpire().equals("1")) {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://" + PcConstant.ADDRESS + "/app/superscanPH/loginPH.jsp";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("m", "login");
            map.add("username", user.getAccount());
            map.add("password", user.getPassword());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
            Object result = jsonObject.get("result");
            if (null != result) {
                if ("lpe".equals(result.toString())) {
                    user.setUseStatus(PcConstant.USE_STATUS_PASSWORD_ERROR);
//                        zyjUserMapper.updateById(user);
                    zyjUserMapper.updateUserStatusByAccount(user.getAccount());
                    return false;
                } else {
                    String s = response.getHeaders().get("Set-Cookie").get(0);
                    user.setCookie(s.split(";")[0] + ";");
                    user.setExpire("0");
                    zyjUserMapper.updateById(user);
                    return true;
                }
            } else {
                String s = response.getHeaders().get("Set-Cookie").get(0);
                user.setCookie(s.split(";")[0] + ";");
                user.setExpire("0");
                zyjUserMapper.updateById(user);
                return true;
            }
        } else {
            return true;
        }
    }

    public void setQuerySequence(ZyjUser user) {
        //获取查询次数
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://" + PcConstant.ADDRESS + "/app/superscanPH/opQuery.jsp";
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        //在 header 中存入cookies
        cookies.add(user.getCookie());
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("m", "mine");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        JSONObject jsonObject = JSONObject.parseObject(response.getBody());
        if ("ns".equals(jsonObject.get("result"))) {
            //cookies过期
            this.setCookies(user);
            cookies.add(user.getCookie());
            headers.put(HttpHeaders.COOKIE, cookies);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            map.add("m", "mine");
            response = restTemplate.postForEntity(url, request, String.class);
            jsonObject = JSONObject.parseObject(response.getBody());
            String sycs = (String) jsonObject.get("qinf");
            String leaveNum = sycs.split("/")[1];
            user.setLeaveNum(leaveNum);
        } else {
            String sycs = (String) jsonObject.get("qinf");
            String leaveNum = sycs.split("/")[1];
            user.setLeaveNum(leaveNum);
        }
    }

    public boolean isNext(ZyjUser user) {
        if (PcConstant.USE_STATUS_TY.equals(user.getUseStatus()) || PcConstant.USE_STATUS_PASSWORD_ERROR.equals(user.getUseStatus())) {
            return false;
        } else {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://" + PcConstant.ADDRESS + "/app/superscanPH/opQuery.jsp";
            HttpHeaders headers = new HttpHeaders();
            List<String> cookies = new ArrayList<String>();
            /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
            //在 header 中存入cookies
            cookies.add(user.getCookie());
            headers.put(HttpHeaders.COOKIE, cookies);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("m", "mine");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
            if ("ns".equals(jsonObject.get("result"))) {
                //cookies过期
                cookies.clear();
                user.setExpire("1");
                boolean b = this.setCookies(user);
                if (false == b) {
                    return false;
                }
                cookies.add(user.getCookie());
                headers.put(HttpHeaders.COOKIE, cookies);
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                map.add("m", "mine");
                response = restTemplate.postForEntity(url, request, String.class);
                jsonObject = JSONObject.parseObject(response.getBody());
                String sycs = (String) jsonObject.get("qinf");
                String leaveNum = sycs.split("/")[1];
                String substring = leaveNum.substring(2);
                String day = substring.substring(0, substring.indexOf("次"));
                int num = Integer.parseInt(day);
                user.setLeaveNum(leaveNum);
                if (num == PcConstant.TIMES_180 || num == PcConstant.TIMES_100 || num == PcConstant.TIMES_20 || num == PcConstant.TIMES_1) {
                    user.setUseStatus(PcConstant.USE_STATUS_TY);
                    zyjUserMapper.updateById(user);
                    return false;
                } else if (user.getMaxTimes() < num) {
                    user.setUseStatus(PcConstant.USE_STATUS_YSY);
                    return true;
                } else {
                    user.setUseStatus(PcConstant.USE_STATUS_TY);
                    zyjUserMapper.updateById(user);
                    return false;
                }
            } else {
                String sycs = (String) jsonObject.get("qinf");
                String leaveNum = sycs.split("/")[1];
                user.setLeaveNum(leaveNum);
                String substring = leaveNum.substring(2);
                String day = substring.substring(0, substring.indexOf("次"));
                int num = Integer.parseInt(day);
                user.setLeaveNum(leaveNum);
                if (num == PcConstant.TIMES_180 || num == PcConstant.TIMES_100 || num == PcConstant.TIMES_20 || num == PcConstant.TIMES_1) {
                    user.setUseStatus(PcConstant.USE_STATUS_TY);
                    zyjUserMapper.updateById(user);
                    return false;
                } else if (user.getMaxTimes() < num) {
                    user.setUseStatus(PcConstant.USE_STATUS_YSY);
                    return true;
                } else {
                    user.setUseStatus(PcConstant.USE_STATUS_TY);
                    zyjUserMapper.updateById(user);
                    return false;
                }
            }
        }
    }

    @Override
    public Result searchZyj(String searchName, String code) {
        ZyjToken token = zyjTokenMapper.queryTokenByCode(code);
        if (token == null) {
            return Result.fail("激活码失效");
        }
        if (token.getRemainingTimes() < 1) {
            return Result.fail("对不起，您的查询次数已用完");
        }
        if ("2".equals(token.getEnable())) {
            return Result.fail("对不起，您的激活码已到期");
        }
        List<ZyjUser> zyjUsers = zyjUserMapper.queryZyjUsers();
        //查询
        JSONObject jsonObject = null;
        //备用方案
        HhUser hhUser = hhUserMapper.selectTopUser();
        if (PcConstant.USE_STATUS_ENABLE.equals(hhUser.getUseStatus())) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("token", hhUser.getCookies());
            map.add("nick", searchName);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(PcConstant.SPARE_ADDRESS, request, String.class);
            jsonObject = JSONObject.parseObject(response.getBody());
            token.setRemainingTimes(token.getRemainingTimes() - 1);
            zyjTokenMapper.updateById(token);
            System.out.println("内容:" + jsonObject.toString());
        } else {
            //正规查号
            for (ZyjUser user : zyjUsers) {
                if (!this.isNext(user)) {
                    continue;
                }

                RestTemplate restTemplate = new RestTemplate();
                String url = "http://" + PcConstant.ADDRESS + "/app/superscanPH/opQuery.jsp";
                HttpHeaders headers = new HttpHeaders();
                List<String> cookies = new ArrayList<String>();
                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                cookies.add(user.getCookie());
                headers.put(HttpHeaders.COOKIE, cookies);
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                map.add("m", "queryAliim");
                map.add("aliim", searchName);
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
                jsonObject = JSONObject.parseObject(response.getBody());
                Object aa = jsonObject.get("result");
                if (aa == null) {
                    continue;
                }
                String result = aa.toString();
                if ("ns".equals(result)) {
                    //cookies过期
                    user.setExpire("1");
                    boolean b = this.setCookies(user);
                    if (false == b) {
                        continue;
                    }
                    /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
                    //在 header 中存入cookies
                    cookies.add(user.getCookie());
                    headers.put(HttpHeaders.COOKIE, cookies);
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    map.add("m", "queryAliim");
                    map.add("aliim", searchName);
                    request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
                    response = restTemplate.postForEntity(url, request, String.class);
                    jsonObject = JSONObject.parseObject(response.getBody());
                    //需要验证码
                    if ("y".equals(jsonObject.get("iff"))) {
                        continue;
                    }
                } else if ("账号不存在".equals(result)) {
                    break;
                } else {
                    token.setRemainingTimes(token.getRemainingTimes() - 1);
                    zyjTokenMapper.updateById(token);
                    this.setQuerySequence(user);
                    int queryNum = user.getQueryNum();
                    user.setQueryNum(++queryNum);
                    zyjUserMapper.updateById(user);
                    zyjUserMapper.updateUserByAccount(user.getAccount(), user.getQueryNum());
//                zyjUserMapper.updateBatchByIds(user.getId(), user.getPort());
                    break;
                }
            }
        }
        if (StringUtils.isEmpty(jsonObject)) {
            Result.succ(200, "账号不存在", jsonObject);
            return Result.succ(jsonObject);
        } else {
            //下载图片到本地
            Map<String,String> returnMap = getPictureUrl(jsonObject);
            returnMap.put("jsonObject",jsonObject.toJSONString());
            return Result.succ(returnMap);
        }

    }


    public Map<String, String> getPictureUrl(JSONObject jsonObject) {
        Map<String,String> map = new HashMap<>();
        map.put("baseImg",(String)jsonObject.get("baseImg"));
        map.put("downImg",(String)jsonObject.get("downImg"));
        URL url = null;
        String downPath = "";
        File file = null;
        String uuid = "";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Host","i.cy1788.com");
        headers.put("Referer","http://49.234.132.215/");
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        Map<String,String> returnMap = new HashMap<>();
        for(Map.Entry<String, String> entry : map.entrySet()){
            if("baseImg".equals(entry.getKey())){
                try {
                    returnMap.put("baseImg","");
//                    String baseImgPath = path + "baseImg";
//                    file = new File(baseImgPath);
//                    if(!file.exists()){
//                        file.mkdir();
//                    }
//                    String baseImgUuid = UUIDUtil.getUUID();
//                    downPath = baseImgPath + "/" + baseImgUuid + ".png";
//                    returnMap.put("baseImg",baseImgUuid + ".png");
                    downloadImageWithHeaders(returnMap,entry.getValue(),"PNG",new File(downPath),headers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if("downImg".equals(entry.getKey())){
                try {
                    returnMap.put("downImg","");
//                    String baseImgPath = path + "downImg";
//                    file = new File(baseImgPath);
//                    if(!file.exists()){
//                        file.mkdir();
//                    }
//                    String downImgUuid = UUIDUtil.getUUID();
//                    downPath = baseImgPath + "/" + downImgUuid + ".png";
//                    returnMap.put("downImg",downImgUuid + ".png");
                    downloadImageWithHeaders(returnMap,entry.getValue(),"PNG",new File(downPath),headers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return returnMap;
    }

    public static Map<String,String> downloadImageWithHeaders(Map<String,String> returnMap, String imageUrl, String formatName, File localFile, Map<String, String> headers) {
        boolean isSuccess = false;
        InputStream stream = null;
        try {
            URL url = new URL(imageUrl);
            URLConnection conn = url.openConnection();
            if (headers != null && !headers.isEmpty()) {
                //设置头信息
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            conn.setDoInput(true);
            stream = conn.getInputStream();
            BufferedImage bufferedImg = ImageIO.read(stream);
            if (bufferedImg != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImg, "jpg", baos);
                byte[] bytes = baos.toByteArray();
                String s = encoder.encodeBuffer(bytes).trim();
                if(returnMap.containsKey("baseImg")){
                    returnMap.put("baseImg",s);
                }else if(returnMap.containsKey("downImg")){
                    returnMap.put("downImg",s);
                }
//                isSuccess = ImageIO.write(bufferedImg, formatName, localFile);
            } else {
                throw new RuntimeException("图片[" + imageUrl + "]下载失败");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnMap;
    }

    @Override
    public Result searchMarking(String searchName, String code) {
        ZyjToken token = zyjTokenMapper.queryTokenByCode(code);
        if (token == null) {
            return Result.fail("无效的激活码");
        }
//        if(token.getRemainingTimes() < 1){
//            return Result.fail("对不起，您的查询次数已用完");
//        }
        if ("2".equals(token.getEnable())) {
            return Result.fail("对不起，您的激活码已到期");
        }
        //调用登录接口获取cookie查询次数
        List<ZyjUser> zyjUsers = zyjUserMapper.queryZyjUsers();
        JSONObject jsonObject = null;
        for (ZyjUser user : zyjUsers) {
            if (!this.isNext(user)) {
                continue;
            }
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://" + PcConstant.ADDRESS + "/app/superscanPH/opQuery.jsp";
            HttpHeaders headers = new HttpHeaders();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            List<String> cookies = new ArrayList<String>();
            /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
            //在 header 中存入cookies
            cookies.add(user.getCookie());
            headers.put(HttpHeaders.COOKIE, cookies);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            map.add("m", "getQWRs");
            map.add("wxorqq", searchName);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            jsonObject = JSONObject.parseObject(response.getBody());
//            String result = jsonObject.get("result").toString();
            Object aa = jsonObject.get("result");
            if (aa == null) {
                continue;
            }
            String result = aa.toString();
            if ("ns".equals(result)) {
                //token过期
                //cookies过期
                cookies.clear();
                user.setExpire("1");
                this.setCookies(user);
                cookies.add(user.getCookie());
                headers.put(HttpHeaders.COOKIE, cookies);
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                map.add("m", "getQWRs");
                map.add("wxorqq", searchName);
                jsonObject = JSONObject.parseObject(response.getBody());
                //需要验证码
                if ("y".equals(jsonObject.get("iff"))) {
                    continue;
                }
            } else if ("wxnodata".equals(result)) {
                break;
            } else {
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
        } catch (Exception e) {
            log.error("查询token出现异常:" + e.toString());
            return Result.fail("查询token出现异常:" + e.toString());
        }
    }

    public List<TokenDto> setCookie(List<ZyjUser> zyjUsers) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://106.12.189.59/app/superscanPH/loginPH.jsp";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        List<TokenDto> tokenDtos = new ArrayList<>();
        for (ZyjUser zyjUser : zyjUsers) {
            TokenDto tokenDto = new TokenDto();
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("m", "login");
            map.add("username", zyjUser.getAccount());
            map.add("password", zyjUser.getPassword());
            tokenDto.setAccount(zyjUser.getAccount());
            tokenDto.setPassword(zyjUser.getPassword());
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            String s = response.getHeaders().get("Set-Cookie").get(0);
            tokenDto.setCookie(s.split(";")[0] + ";");
            this.setMy(tokenDto);
            //再查我的
            tokenDtos.add(tokenDto);
        }
        return tokenDtos;
    }

    public void setMy(TokenDto tokenDto) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://106.12.189.59/app/superscanPH/opQuery.jsp";
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<String>();
        /* 登录获取Cookie 这里是直接给Cookie，可使用下方的login方法拿到Cookie给入*/
        //在 header 中存入cookies
        cookies.add(tokenDto.getCookie());
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("m", "mine");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        Object parse = JSONObject.parse(response.getBody());
        tokenDto.setObj(parse);
    }

    @Override
    public Result getZyjMy() {
        //查询所有的账号
        List<ZyjUser> zyjUsers = zyjUserMapper.queryZyjUsers();
        if (CollectionUtils.isEmpty(zyjUsers)) {
            return null;
        }
        //设置cookies
        List<TokenDto> tokenDtos = this.setCookie(zyjUsers);
        return Result.succ(tokenDtos);
    }

    @Override
    public Result exportAllExcel(HttpServletRequest request, HttpServletResponse response, String typeId) {
        try {
            //查询
            List<TokenExcel> zyjTokens = zyjTokenMapper.queryNotEnableTokens(typeId);
            List<String> codes = new ArrayList<>();
            for (TokenExcel token : zyjTokens) {
//                String prifx = "http://182.92.126.206/#/HomePage?code=";
                String prifx = "https://zyjch.vip/#/HomePage?code=";

                codes.add(token.getCode());
                token.setCode(prifx + token.getCode());
            }
            if (!CollectionUtils.isEmpty(codes)) {
                zyjTokenMapper.updateBatchExportStatus(codes);
            }
            return Result.succ(FileDownloadUtil.export(response, zyjTokens, TokenExcel.class));
        } catch (Exception e) {
            log.error("导出数据出现异常，异常原因:" + e.toString());
            return Result.fail("导出数据出现异常，异常原因:" + e.toString());
        }

    }
}
