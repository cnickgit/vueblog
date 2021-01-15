package com.markerhub.common.lang;

import cn.afterturn.easypoi.exception.excel.ExcelExportException;
import com.alibaba.fastjson.JSONObject;
import com.markerhub.constant.PcConstant;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: vueblog
 * @description:
 * @author: wsg
 * @create: 2021-01-15 11:58
 **/
public class Test {
    private static final Integer ONE = 1;
    public FileWriter fwriter = null;

    int count = 0;
    public static void login(String account,FileWriter fwriter){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://"+PcConstant.ADDRESS+"/app/superscanPH/loginPH.jsp";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("m", "login");
            map.add("username", account);
            map.add("password", "123456bbbbbbzzzzzz");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class );
            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
            Object aa = jsonObject.get("result");
            if(aa != null){
                String result = aa.toString();
                if("lpe".equals(result)){
                    fwriter.write(account+",");
                    System.out.println("account------>:"+account);
                }
            }
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa:"+aa.toString());
        }catch (Exception e){

        }

    }


    public static void main(String[] args) {
        FileWriter fwriter = null;
        /* 读取数据 */
        try {
            fwriter = new FileWriter("C:/Users/drj/Desktop/jar包/aa.txt", true);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/Users/drj/Desktop/jar包/132-0.txt")),
                    "UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                login(lineTxt,fwriter);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("read errors :" + e);
        }finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
