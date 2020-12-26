package com.th.convert;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.policy.HackLoopTableRenderPolicy;
import com.th.convert.tl.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
@Controller
public class BookmarkController {
    @Value("${filePath}")
    private String filePath;

    @Value("${fbgmbPath}")
    private String fbgmbPath;

    @Value("${dtablembPath}")
    private String dtablembPath;

    @Value("${dqjcmbPath}")
    private String dqjcmbPath;

    @Value("${convertServer}")
    private String convertServer;


    @RequestMapping("createReport")
    @ResponseBody
    public Map<String, Object> createReport(@RequestBody String dataStr) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("code","1");
        JSONObject jsonObject = JSONObject.parseObject(dataStr);
        if(jsonObject != null)
        {
            //根据日期和UUID生成目录，注意这个目录代表生成一份报告
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String path = filePath + File.separator + sdf.format(d) + File.separator+UUID.randomUUID().toString();
            File dest = new File(path);
            if (!dest.exists()) {
                dest.mkdirs();
            }
            //需要合并的文件
            List<String> fileList = new ArrayList<String>();
            //生成文件的目标目录，这里是0号文件的临时文件，没有图片信息
            String target = path + File.separator +"0Temp.docx";

            if(jsonObject.getJSONObject("data") != null)
            {
                jsonObject = jsonObject.getJSONObject("data");
            }

            if(jsonObject.get("dataStr") != null)
            {
                jsonObject = jsonObject.getJSONObject("dataStr");
            }

            if(jsonObject != null)
            {
                //oa文件的下载地址
                String downloadUrl = jsonObject.getString("downloadUrl");
                //列表文件
                String detailTables = jsonObject.getString("detailLists");
                //已经获取到，为提高json解析效率，删除列表的json数据
                jsonObject.remove("detailLists");

                //---------------------封装0号文件需要的数据，生成0号文件------------------
                PaymentHackData paymentHackData = jsonObject.toJavaObject(PaymentHackData.class);
                List<SafeHiddenTroubles> safeHiddenTroublesList = paymentHackData.getD_safeHiddenTroubles();
                safeHiddenTroublesList.addAll(paymentHackData.getB_safeHiddenTroubles());
                safeHiddenTroublesList.addAll(paymentHackData.getC_safeHiddenTroubles());
                Map<String,PictureRenderData> imgMap = new HashMap<String,PictureRenderData>();
                for(SafeHiddenTroubles safeHiddenTroubles : safeHiddenTroublesList)
                {
                    String[] fjscStrs = safeHiddenTroubles.getFjsc().split(",");
                    for(String fjsc : fjscStrs)
                    {
                        if(fjsc != null && !fjsc.isEmpty())
                        {
                            fjsc = fjsc.replaceAll("\\{","").replaceAll("}","").replaceAll("@","");
                            String fjsc_showname = safeHiddenTroubles.getFjsc_showname();
                            String imgPath = downloadUrl+fjsc;
                            System.out.println(fjsc);
                            System.out.println(fjsc_showname);
                            System.out.println(imgPath);
                            if(fjsc_showname != null && !fjsc_showname.isEmpty())
                            {
                                fjsc_showname = fjsc_showname.substring(fjsc_showname.lastIndexOf("."));
                                imgMap.put(fjsc,new PictureRenderData(220, 220, fjsc_showname, BytePictureUtils.getUrlBufferedImage(imgPath)));
                            }
                        }
                    }
                }
                reportBaseData(fbgmbPath,target,jsonObject);
                //填充图片
                String img_target = path + File.separator +"0.docx";
                reportImgData(target,img_target,imgMap);
                fileList.add(img_target);
                //---------------------封装0号文件需要的数据，生成0号文件------------------


                //---------------------封装列表文件需要的数据，生成列表文件，会生成多个文件------------------
                if(!detailTables.isEmpty())
                {
                    JSONArray detailTablesJson = JSONArray.parseArray(detailTables);
                    for(int i = 0 ; i < detailTablesJson.size() ; i ++)
                    {
                        JSONObject detailTableJsonObj = (JSONObject)detailTablesJson.get(i);
                        String tableType = detailTableJsonObj.getString("tableType");
                        if(!tableType.isEmpty() && tableType.equals("allList"))
                        {
                            DqjcData dqjcData = detailTableJsonObj.toJavaObject(DqjcData.class);
                            String tarPath = path+File.separator+(i+1)+".docx";
                            createTable_dqjc(dqjcData,tarPath);
                            fileList.add(tarPath);
                        }
                        else if(!tableType.isEmpty() && tableType.equals("djTable"))
                        {
                            //生成D级
                            DjTableData djTableData = detailTableJsonObj.toJavaObject(DjTableData.class);
                            String tarPath = path+File.separator+(i+1)+"djtemp.docx";
                            createTable_dj(djTableData,tarPath,downloadUrl);
                            fileList.add(tarPath.replaceAll("djtemp",""));
                        }
                    }
                }
                //---------------------封装列表文件需要的数据，生成列表文件，会生成多个文件------------------


                //---------------------生成附件文件------------------
                String fj = jsonObject.getString("fj");
                if(fj != null && !fj.isEmpty())
                {
                    String[] fjStrs = fj.split(",");
                    String fj_showname = jsonObject.getString("fj_showname");
                    String[] fjNames = fj_showname.split(",");
                    for(int k = 0 ; k < fjStrs.length ; k ++)
                    {
                        String fjname = fjNames[k];
                        fjname = UUID.randomUUID().toString() + fjname.substring(fjname.lastIndexOf("."));
                        String tarPath = path+File.separator+fjname+".docx";
                        HttpUtil.downloadFile(downloadUrl+fjStrs[k], tarPath);
                        fileList.add(tarPath);
                    }
                }
                //---------------------生成附件文件------------------

                //注意这里要调用合并、生成目录接口
                String fileStrs = JSONArray.toJSONString(fileList);
                String tarPath = path+File.separator+UUID.randomUUID().toString()+".docx";
                result = hbFiles(fileStrs,tarPath);
            }
            else
            {
                result.put("errorMsg","参数 data 为空！");
            }
        }
        else
        {
            result.put("errorMsg","参数为空！");
        }


        return result;
    }

    private Map<String,Object> hbFiles(String files,String targetPath){
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            result.put("code", "1");
            AppConfig appConfig = new AppConfig();
            RestTemplate restTemplate = appConfig.restTemplate();
            String url = "http://127.0.0.1:59889/THEditor";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("operateCode", "10046");
            map.add("saveLocalPath", targetPath);
            map.add("updateDocCate", "1");
            map.add("files", files);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            HttpStatus httpStatus = response.getStatusCode();
            String responseBody = response.getBody();
            System.out.println(responseBody);
            if(httpStatus.value() == 200)
            {
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                if(jsonObject.get("code").toString().equals("1"))
                {
                    result.put("code", "0");
                    result.put("downloadUrl",convertServer+ "/" + "download?filePath="+targetPath.replaceAll("\\\\","/"));
                }
            }
        } catch (Exception e) {
            result.put("errorMsg", e.getMessage());
        }
        return result;
    }

    public void reportBaseData(String resourcePath,String targetPath,JSONObject jsonObject) throws IOException {
        HackLoopTableRenderPolicy hackLoopTableRenderPolicy = new HackLoopTableRenderPolicy(true);
        Configure config = Configure.builder()
                .bind("d_safeHiddenTroubles", hackLoopTableRenderPolicy)
                .bind("c_safeHiddenTroubles", hackLoopTableRenderPolicy)
                .bind("b_safeHiddenTroubles", hackLoopTableRenderPolicy)
                .build();
        XWPFTemplate template = XWPFTemplate.compile(resourcePath, config).render(jsonObject.toJavaObject(PaymentHackData.class));
        template.writeToFile(targetPath);
    }

    public void reportImgData(String resourcePath,String targetPath,Map<String,PictureRenderData> imgMap) throws IOException {
        XWPFTemplate.compile(resourcePath).render(imgMap)
                .writeToFile(targetPath);
    }

    public void createTable_dqjc(DqjcData dqjcData, String targetPath) throws IOException {
        HackLoopTableRenderPolicy hackLoopTableRenderPolicy = new HackLoopTableRenderPolicy(true);
        Configure config = Configure.builder()
                .bind("dqjcTables", hackLoopTableRenderPolicy)
                .build();
        XWPFTemplate template = XWPFTemplate.compile(dqjcmbPath, config).render(dqjcData);
        template.writeToFile(targetPath);
    }

    public void createTable_dj(DjTableData djTableData, String targetPath, String downloadUrl) throws IOException {

        Map<String,PictureRenderData> imgMap = new HashMap<String,PictureRenderData>();
        for(DjTableListData safeHiddenTroubles : djTableData.getDjmsTables())
        {
            String[] fjscStrs = safeHiddenTroubles.getFjsc().split(",");
            for(String fjsc : fjscStrs)
            {
                if(fjsc!= null && !fjsc.isEmpty())
                {
                    fjsc = fjsc.replaceAll("\\{","").replaceAll("}","").replaceAll("@","");
                    String fjsc_showname = safeHiddenTroubles.getFjsc_showname();
                    String imgPath = downloadUrl+fjsc;
                    if(fjsc_showname != null && !fjsc_showname.isEmpty())
                    {
                        fjsc_showname = fjsc_showname.substring(fjsc_showname.lastIndexOf("."));
                        imgMap.put(fjsc,new PictureRenderData(220, 220, fjsc_showname, BytePictureUtils.getUrlBufferedImage(imgPath)));
                    }
                }
            }
        }

        HackLoopTableRenderPolicy hackLoopTableRenderPolicy = new HackLoopTableRenderPolicy(true);
        Configure config = Configure.builder()
                .bind("djmsTables", hackLoopTableRenderPolicy)
                .build();
        XWPFTemplate template = XWPFTemplate.compile(dtablembPath, config).render(djTableData);
        template.writeToFile(targetPath);

        //填充图片
        String img_target = targetPath.replaceAll("djtemp","");
        reportImgData(targetPath,img_target,imgMap);
    }
}
