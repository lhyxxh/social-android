package com.eastcom.social.pos.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;

/*
 * 时间工具类
 * author:wood
 * */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	public static String formatWBTime(String created) {
		String s = "";
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date = dateFormat.parse(created);
			Long l = date.getTime();
			Long now = System.currentTimeMillis();
			if ((now - l) < (1000 * 60)) {
				if (((now - l) / 60) > 0)
					s = (now - l) / 60 + "分钟前";
				else
					s = ((now - l) % 60) + "秒前";
			} else {
				s = dateFormat2.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static String formateSource(String str) {
		String s = "";
		String[] ss = str.split(">");
		s = ss[1].split("<")[0];

		return s;
	}

	public static Date getDateTime(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date currentTime = formatter.parse(date, pos);
		return currentTime;
	}

	public static Date getDay(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		ParsePosition pos = new ParsePosition(0);
		Date currentTime = formatter.parse(date, pos);
		return currentTime;
	}

	public static Date getDayOfMonth(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-01 00:00:00");
		ParsePosition pos = new ParsePosition(0);
		Date currentTime = formatter.parse(date, pos);
		return currentTime;
	}

	public static Date getDayOfYear(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-01-01 00:00:00");
		ParsePosition pos = new ParsePosition(0);
		Date currentTime = formatter.parse(date, pos);
		return currentTime;
	}

	public static Date getDateTime2(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		ParsePosition pos = new ParsePosition(0);
		Date currentTime = formatter.parse(date, pos);
		return currentTime;
	}

	public static Date getDate(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		Date currentTime = formatter.parse(date, pos);
		return currentTime;
	}

	public static Date getDayOfMonth(int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, month, 1);
		return cal.getTime();
	}

	public static String getDateTinyString(Date date, int beforeOrAfterDay) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 加减天
		cal.add(Calendar.DATE, beforeOrAfterDay);
		return format.format(cal.getTime());
	}

	public static String getCurrentDateTimeString() {
		return getDateTimeString(System.currentTimeMillis());
	}

	public static String getDateString(Date date, int beforeOrAfterDay) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 加减天
		cal.add(Calendar.DATE, beforeOrAfterDay);
		return format.format(cal.getTime());
	}
	
	

	/**
	 * 前几天
	 * 
	 * @param date
	 * @param beforeOrAfterDay
	 * @return
	 */
	public static Date getDate(Date date, int beforeOrAfterDay) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 加减天
		cal.add(Calendar.DATE, beforeOrAfterDay);
		return cal.getTime();
	}

	@SuppressWarnings("deprecation")
	public static Date getLastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date lastDate = cal.getTime();
		lastDate.setDate(lastDay);
		return lastDate;
	}

	public static String getDateStringZeroTime(Date date, int beforeOrAfterDay) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 加减天
		cal.add(Calendar.DATE, beforeOrAfterDay);
		return format.format(cal.getTime());
	}

	public static String getDateStringBeforeDay(Date date, int beforeOrAfterDay) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 加减天
		cal.add(Calendar.DATE, beforeOrAfterDay);
		return format.format(cal.getTime());
	}

	public static String getHourStringZeroTime(Date date, int beforeOrAfterHour) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 加减天
		cal.add(Calendar.HOUR, beforeOrAfterHour);
		return format.format(cal.getTime());
	}

	public static String getHourShortStringZeroTime(Date date,
			int beforeOrAfterHour) {
		SimpleDateFormat format = new SimpleDateFormat("00:00:00");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 加减天
		cal.add(Calendar.HOUR, beforeOrAfterHour);
		return format.format(cal.getTime());
	}

	public static String getDateString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}
	public static String getWholeDateString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

	public static String getChineseDateTinyString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
		return format.format(date);
	}

	public static String getDateString(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date(time));
	}

	public static String getDateTimeString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(date);
	}

	public static String getDateTimeString(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(new Date(time));
	}

	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}

	public static String getHourAndMin(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(date);
	}

	public static String getChatTime(String time) {
		return getChatTime(getDateTime(time).getTime());
	}

	public static String getChatTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天 " + getHourAndMin(timesamp);
			break;
		case 1:
			result = "昨天 " + getHourAndMin(timesamp);
			break;
		case 2:
			result = "前天 " + getHourAndMin(timesamp);
			break;

		default:
			// result = temp + "天前 ";
			result = getDateTimeString(timesamp);
			break;
		}

		return result;
	}

	/**
	 * 
	 * 时间戳与实践类型转换
	 * 
	 * */

	/**
	 * 时间戳转换成日期格式字符串
	 * 
	 * @param seconds
	 *            精确到秒的字符串
	 * @param formatStr
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/**
	 * 日期格式字符串转换成时间戳
	 * 
	 * @param date
	 *            字符串日期
	 * @param format
	 *            如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2TimeStamp(String date_str, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(date_str).getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 取得当前时间戳（精确到秒）
	 * 
	 * @return
	 */
	public static String timeStamp() {
		long time = System.currentTimeMillis();
		String t = String.valueOf(time / 1000);
		return t;
	}
	
	/**
	 * 加减秒
	 * @param date
	 * @param beforeOrAfterSeconds
	 * @return
	 */
	public static String getDateAfterSecond(Date date, int beforeOrAfterSecond) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, beforeOrAfterSecond);
		return format.format(cal.getTime());
	}
	
	public static boolean betweenTime(int start , int end) {
		Calendar cal = Calendar.getInstance();// 当前日期
		int hour = cal.get(Calendar.HOUR_OF_DAY);// 获取小时
		int minute = cal.get(Calendar.MINUTE);// 获取分钟
		int minuteOfDay = hour * 60 + minute;// 从0:00分开是到目前为止的分钟数
		if (minuteOfDay >= start && minuteOfDay <= end) {
			return true;
		} else {
			return false;
		}
	}

	// 输出结果：
	// timeStamp=1417792627
	// date=2014-12-05 23:17:07
	// 1417792627
	public static void main(String[] args) {
		String timeStamp = timeStamp();
		System.out.println("timeStamp=" + timeStamp);

		String date = timeStamp2Date(timeStamp, "yyyy-MM-dd HH:mm:ss");
		System.out.println("date=" + date);

		String timeStamp2 = date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss");
		System.out.println(timeStamp2);
	}
}
