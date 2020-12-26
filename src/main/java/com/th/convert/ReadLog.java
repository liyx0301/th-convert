package com.th.convert;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class ReadLog {
    public static void main(String[] args){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/liyingxin/test/log/206.log"), "GB2312"));
            List<String> exception = br.lines().peek(e -> {
            }).filter(e -> {
                return e.contains("ImageFile_Insert ");
            }).collect(Collectors.toList());
            br.close();
            for(String errLine : exception)
            {
                String temStr = errLine.substring(errLine.indexOf("ImageFile_Insert"));
                System.out.println(temStr.replaceAll("ImageFile_Insert ",""));
                String[] strs = temStr.split(getSeparator()+"");
                if(strs.length == 8)
                {
                    //System.out.println(strs);
                }
            }
            System.out.println("===============");
            System.out.println(exception.size());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static char getSeparator() {
        return 2;
    }
}
