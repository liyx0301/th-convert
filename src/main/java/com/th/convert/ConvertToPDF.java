package com.th.convert;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class ConvertToPDF {


    public Map<String,Object> convert(String filePath,String pdfFilePath,String convertServer,String fileRootPath,String batPath){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.put("code", "1");
            AppConfig appConfig = new AppConfig();
            RestTemplate restTemplate = appConfig.restTemplate();
            String url = "http://127.0.0.1:59889/THEditor";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("operateCode", "10041");
            map.add("saveLocalPath", pdfFilePath);
            map.add("openLocalPath", filePath);
            map.add("FileType",getFileType(filePath));

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            HttpStatus httpStatus = response.getStatusCode();
            System.out.println("===================="+httpStatus);
            String responseBody = response.getBody();
            System.out.println(responseBody);
            if(httpStatus.value() == 200)
            {
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                System.out.println("====================jsonObject:"+jsonObject.get("code"));
                if(jsonObject.get("code").toString().equals("1"))
                {
                    result.put("code", "0");
                    System.out.println("====================11111:"+result.get("code"));
                    result.put("pdfUrl",convertServer+ "/" + "download?filePath="+pdfFilePath.replaceAll("\\\\","/"));
                    System.out.println("====================222222:"+result.get("pdfUrl"));
                }
                return result;
            }
            else {
                Utils.killEditor(batPath);
                Thread.sleep(3000);
                convert(filePath,pdfFilePath,convertServer,fileRootPath,batPath);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if(e.getMessage().indexOf("java.net.ConnectException: Connection refused") > -1)
            {
                System.out.println("=============111==============");
                Utils.killEditor(batPath);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                convert(filePath,pdfFilePath,convertServer,fileRootPath,batPath);
            }
            else
            {
                result.put("errorMsg", e.getMessage());
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    public String getFileType(String fileName)
    {
        if(fileName.toLowerCase().endsWith(".doc") || fileName.toLowerCase().endsWith(".docx"))
        {
            return "Word";
        }

        if(fileName.toLowerCase().endsWith(".xls") || fileName.toLowerCase().endsWith(".xlsx"))
        {
            return "Excel";
        }

        if(fileName.toLowerCase().endsWith(".ppt") || fileName.toLowerCase().endsWith(".pptx"))
        {
            return "PPT";
        }
        return "";
    }
}