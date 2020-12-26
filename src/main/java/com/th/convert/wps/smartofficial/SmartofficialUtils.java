package com.th.convert.wps.smartofficial;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class SmartofficialUtils {

    public String smartofficial(String viewDomain,String fileUrl,String templateUrl) throws IOException {
        JSONObject result = new JSONObject();
        try
        {
            //上传文件，获取文件ID
            String apiUrl = viewDomain + "web-preview/api/httpFile";
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("url", fileUrl));
            String postResult = doPost(apiUrl,params,null);
            String fileId = getResult(postResult,"id");
            if(!fileId.isEmpty())
            {
                apiUrl = viewDomain + "web-preview/api/httpFile";
                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("url", templateUrl));
                postResult = doPost(apiUrl,params,null);
                String templateId = getResult(postResult,"id");
                if(!templateId.isEmpty())
                {
                    apiUrl =  viewDomain + "web-preview/api/feature/smartofficial";
                    JSONObject jsonObjectParams = new JSONObject();
                    jsonObjectParams.put("id",fileId);
                    jsonObjectParams.put("templateId",templateId);
                    postResult = doPost(apiUrl,null,jsonObjectParams.toJSONString());
                    String downloadUrl =  viewDomain + "web-preview/api/download/"+getResult(postResult,"download");
                    if(!downloadUrl.isEmpty())
                    {
                        result.put("api_status",true);
                        result.put("downloadUrl",downloadUrl);
                    }
                    else
                    {
                        result.put("api_status",false);
                        result.put("errorMsg","smartofficial error!");
                    }
                }
                else
                {
                    result.put("api_status",false);
                    result.put("errorMsg","templateUrl upload error!");
                }
            }
            else
            {
                result.put("api_status",false);
                result.put("errorMsg","fileUrl upload error!");
            }
        }
        catch (Exception ex)
        {
            result.put("api_status",false);
            result.put("errorMsg",ex.getMessage());
        }
        System.out.println(result.toJSONString());
        return result.toJSONString();
    }

    public String doPost(String apiUrl,List<NameValuePair> params,String dataJson) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse httpResponse = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(20000).setSocketTimeout(20000).build();
        httpPost.setConfig(requestConfig);

        if(params != null)
        {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, String.valueOf(Charset.forName("UTF-8")));
            httpPost.setEntity(entity);
        }

        if(dataJson != null)
        {
            StringEntity stringentity = new StringEntity(dataJson,ContentType.create("application/json", "UTF-8"));
            httpPost.setEntity(stringentity);
        }

        httpResponse = httpClient.execute(httpPost);
        HttpEntity responseEntity = httpResponse.getEntity();
        if(responseEntity!=null){
            String content = EntityUtils.toString(responseEntity,"UTF-8");
            return content;
        }
        if(httpResponse!=null){
            httpResponse.close();
        }
        if(httpClient!=null){
            httpClient.close();
        }
        return "";
    }

    public static void main(String args[]) throws IOException {
        SmartofficialUtils smartofficialUtils = new SmartofficialUtils();

        smartofficialUtils.smartofficial("http://47.114.178.18/","http://wv.e-cology.com.cn/wpsSoft/gwzw.docx","http://wv.e-cology.com.cn/wpsSoft/pbmb.dotx");
    }

    public String getResult(String content,String param) throws IOException {
        String paramValue = "";
        JSONObject jsonObject = JSONObject.parseObject(content);
        String code = jsonObject.getString("code");
        String msg = jsonObject.getString("msg");
        String servertime = jsonObject.getString("servertime");
        if(code.equals("200"))
        {
            jsonObject = jsonObject.getJSONObject("data");
            if(!jsonObject.isEmpty())
            {
                paramValue = jsonObject.getString(param);
            }
        }
        return paramValue;
    }
}
