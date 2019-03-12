package com.eastcom.social.pos.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static Date strToDate(String date) throws ParseException {
        return strToDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date strToDate(String date, String formate) throws ParseException {
        if (null == date || "".equals(date)){
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(formate);

        return formatter.parse(date);
    }

    public static String dateToStr(Date date){
        return dateToStr(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String dateToStr(Date date, String formate){
        if (null == date){
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(formate);

        return formatter.format(date);
    }
}
