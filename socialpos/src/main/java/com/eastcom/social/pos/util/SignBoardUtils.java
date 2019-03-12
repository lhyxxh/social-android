package com.eastcom.social.pos.util;

import java.io.DataOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignBoardUtils {
	
	/**
    *
    * 修改一体机系统时间（root）
    * @param datetime 测试的设置的时间【时间格式 yyyyMMddHHmmss】
    */
   public static void changeSystemTime(String datetime) {
       try {
           Process process = Runtime.getRuntime().exec("su");
           Matcher matcher = Pattern.compile("[^0-9]]").matcher(datetime);
           datetime = matcher.replaceAll("").trim().substring(0, 8) + "."
                   + matcher.replaceAll("").trim().substring(8, 14);
           DataOutputStream os = new DataOutputStream(process.getOutputStream());
           os.writeBytes("setprop persist.sys.timezone Asia/Shanghai\n");
           os.writeBytes("/system/bin/date -s " + datetime + "\n");
           os.writeBytes("clock -w\n");
           os.writeBytes("exit\n");
           os.flush();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }


}
