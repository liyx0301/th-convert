package com.th.convert;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableScheduling
public class SaticScheduleTask {

    @Value("${filePath}")
    private String filePath;

    @Value("${batPath}")
    private String batPath;

    @Scheduled(cron = "0 0 1 * * ?")
    //或直接指定时间间隔
    private void configureTasks() {
        File file = new File(filePath);
        Utils.killEditor(batPath);
        deleteFolder(file);
    }

    //需要注意的是当删除某一目录时，必须保证该目录下没有其他文件才能正确删除，否则将删除失败。
    private void deleteFolder(File folder) {
        if (folder.exists()) {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (!file.getPath().toLowerCase().endsWith(sdf.format(d))) {
                            //递归直到目录下没有文件
                            deleteFolder(file);
                        }
                    } else {
                        //删除
                        file.delete();
                    }
                }
            }
            //删除
            folder.delete();
        }
    }
}