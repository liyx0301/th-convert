package com.th.convert;

import java.io.File;

public class Utils {

    public static void killEditor(String batPath)
    {
        Runtime rn = Runtime.getRuntime();
        Process p = null;
        try {
            if(!batPath.isEmpty())
            {
                System.out.println("================cmd bat:"+batPath);
                p = rn.exec("cmd.exe /C \""+batPath+"\"");
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
