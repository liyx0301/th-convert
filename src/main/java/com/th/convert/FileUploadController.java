package com.th.convert;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.hutool.http.HttpUtil;

@Controller
public class FileUploadController {

    @Value("${filePath}")
    private String filePath;

    @Value("${batPath}")
    private String batPath;

    @Value("${convertServer}")
    private String convertServer;
    private static List<String> cList = new ArrayList<String>();

    @RequestMapping("convertDemo")
    public String convertDemo(){
        return "convertDemo";
    }

    @RequestMapping("convert")
    @ResponseBody
    public Map<String,Object> convert(@RequestParam("downloadUrl") String downloadUrl,@RequestParam("fileName") String fileName){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.put("code", "1");
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String path = filePath + File.separator + sdf.format(d) + File.separator;
            File dest = new File(path);
            if (!dest.exists()) {
                dest.mkdirs();
            }
            String uuidName = UUID.randomUUID().toString();
            String pdfFilePath = path+uuidName+".pdf";
            path = download(path,downloadUrl,fileName);

            ConvertToPDF convertToPDF = new ConvertToPDF();
            result = convertToPDF.convert(path,pdfFilePath,convertServer,filePath,batPath);
        } catch (IllegalStateException e) {
            result.put("errorMsg", e.getMessage());
        }
        return result;
    }

    @RequestMapping("convertLocalFile")
    @ResponseBody
    public Map<String,Object> convertLocalFile(@RequestParam("sourceFile") String sourceFile,@RequestParam("targetFile") String targetFile){
        Map<String, Object> result = new HashMap<String, Object>();
        boolean flag = true;
        try {
            cList.add(sourceFile);
            System.out.println(cList.size());
            do
            {
                if(cList.get(0).equals(sourceFile))
                {
                    result.put("code", "1");
                    ConvertToPDF convertToPDF = new ConvertToPDF();
                    result = convertToPDF.convert(sourceFile,targetFile,convertServer,filePath,batPath);
                    System.out.println("======result1111:"+result.get("code"));

                    File targetFileFile = new File(targetFile);
                    if(targetFileFile.exists())
                    {
                        if(targetFileFile.length() > 0)
                        {
                            result.put("code", "0");
                        }
                    }

                    Thread.sleep(1000);
                    cList.remove(sourceFile);
                    return result;
                }
                Thread.sleep(1000);
            } while (cList.size() > 0);
        } catch (IllegalStateException e) {
            result.put("errorMsg", e.getMessage());
            cList.remove(sourceFile);
        } catch (InterruptedException e) {
            result.put("errorMsg", e.getMessage());
            cList.remove(sourceFile);
        }
        return result;
    }

    public static String download(String filePath, String fileUrl,String fileName) {
        if (fileUrl != null) {
            try {
                String uuidName = UUID.randomUUID().toString();
                filePath = filePath + uuidName +fileName;
                HttpUtil.downloadFile(fileUrl, filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }
}