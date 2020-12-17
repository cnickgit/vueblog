package com.markerhub.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @program: esgov-csfw-sqdaservice
 * @description:
 * @author: wsg
 * @create: 2020-04-03 16:01
 **/
@Slf4j
public class FileDownloadUtil<T> {

    public static String downloadFile(HttpServletResponse response, String fileTrueName){
        InputStream inputStream = null;
        ByteArrayOutputStream os = null;
        try {
            //路径名+文件名定位文件所在的位置
            ClassPathResource classPathResource = new ClassPathResource("static/"+fileTrueName);
            inputStream = classPathResource.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            response.setHeader("content-Type","application/vnd.ms-excel");
            response.setHeader("Content-Disposition","attachment;filename="+System.currentTimeMillis()+".xls");
            response.setCharacterEncoding("UTF-8");
            os = new ByteArrayOutputStream();
            workbook.write(os);
            byte[] content = os.toByteArray();
            return Base64.encodeBase64String(content);
        } catch (IOException e) {
            log.error("下载模板出现异常: {}",e.toString());
        }finally {
            try {
                inputStream.close();
                os.close();
            } catch (IOException e) {
                log.error("关闭文件流出现异常: {}",e.toString());
                e.printStackTrace();
            }
        }
        return null;
    }

    public static<T> String export(HttpServletResponse response, List<T> list, Class<?> clazz){
        try {
            ExportParams params = new ExportParams();
            Workbook workbook = ExcelExportUtil.exportExcel(params,clazz, list);
            response.setHeader("content-Type","application/vnd.ms-excel");
            response.setHeader("Content-Disposition","attachment;filename="+System.currentTimeMillis()+".xls");
            response.setCharacterEncoding("UTF-8");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            byte[] content = os.toByteArray();
            return Base64.encodeBase64String(content);
        } catch (Exception e) {
            log.error("下载失败的excel记录出现异常 {}",e.toString());
            return null;
        }
    }
}
