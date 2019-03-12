package com.eastcom.social.pos.config;

import android.os.Environment;

public class Constance {

	public static final String BROADCAST_ACTION = "nodata";
	public static final String PING_STATUS = "ping_status";
	public static final String CLOCK_ACTION = "clock";
	public static final String UPDATE_ACTION = "update";
	public static final String RFSAM_STATUS = "rfsam_status";
	public static boolean liandi = true;
	public static boolean START_SERVICE = false;

	public final static String PolicyFolderPath = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/word/";

//	public static final String API_HOST = "http://120.25.230.37/socialweb-sdbz";// 滨州体验环境
//	public static String host = "120.25.230.37";// 滨州测试
//	public static int port = 11114;

	 public static String host = "120.24.72.174";// 滨州正式
	 public static int port = 2333;
	 public static final String API_HOST =
	 "http://120.24.242.201/socialweb-sdbz";// 滨州正式环境

//	 public static String host = "120.25.230.37";// 滨州正式
//	 public static int port = 10000;
//	 public static final String API_HOST =
//	 "http://120.25.230.37/socialweb-sdbz";// 滨州正式环境

//	 public static String host = "192.168.96.116";// 运维正式
//	 public static int port = 11111;// 运维正式
//	 public static final String API_HOST =
//	 "http://192.168.93.70:8080/social-web-sdbz";// 滨州正式环境
	 public static final String API_FINGER_VEIN =
			 "http://183.63.14.204:19021";// 指静脉后台地址
	public final static String ApkDownloadFile = "/sdcard/app/";
	public final static String ApkIncrementFile = "/sdcard/app/increment/";
}
