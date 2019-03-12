package com.eastcom.social.pos.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LocalDataFactory {

	public static int LocalMainVersion = 9;   //更新记得修改这个版本号
	public static int LocalSubVersion = 6;
	private static String DATA_PATH = "SOCIAL";
	public static String LOCAL_HIGH_BLACK_LIST = "LOCAL_HIGH_BLACK_LIST";
	public static String LOCAL_LOW_BLACK_LIST = "LOCAL_LOW_BLACK_LIST";
	public static String SERVER_HIGH_BLACK_LIST = "SERVER_HIGH_BLACK_LIST";
	public static String SERVER_LOW_BLACK_LIST = "SERVER_LOW_BLACK_LIST";

	public static String HIGH_BLACK_LIST_DONE = "HIGH_BLACK_LIST_DONE";
	public static String LOW_BLACK_LIST_DONE = "LOW_BLACK_LIST_DONE";

	public static String HIGH_BLACK_LIST_NO = "HIGH_BLACK_LIST_NO";
	public static String LOW_BLACK_LIST_NO = "LOW_BLACK_LIST_NO";

	public static String UPDATE_BLACK_LIST = "UPDATE_BLACK_LIST";
	public static String CONFIRM_RESP = "CONFIRM_RESP";

	public static String HOST = "HOST";
	public static String POST = "POST";
	public static String EID = "EID";
	public static String WHOLE_EID = "WHOLE_EID";

	public static String DELETELOG = "DELETELOG";

	public static String RFSAM_STATUS = "RFSAM_STATUS";
	public static String RFSAM_LEFT_INTERVAL = "RFSAM_LEFT_INTERVAL";

	public static String RFSAM_VALID_TIME = "RFSAM_VALID_TIME";

	public static String POLICY_VERSION = "POLICY_VERSION";

	public static String IS_ENCRYPT = "IS_ENCRYPT";

	public static String TRADE_NO = "TRADE_NO";

	public static String VERSION_MAIN_UPDATE = "VERSION_MAIN_UPDATE";// 服务器需要更新的大版本
	public static String VERSION_SUB_UPDATE = "VERSION_SUB_UPDATE";// 服务器需要更新的小版本
	public static String VERSION_UPDATE_SIZE = "VERSION_UPDATE_SIZE";// 服务器需要更新的当前包号

	public static String POLICY_VERSION_MAIN_UPDATE = "POLICY_VERSION_MAIN_UPDATE";
	public static String POLICY_VERSION_SUB_UPDATE = "POLICY_VERSION_SUB_UPDATE";
	public static String POLICY_VERSION_UPDATE_SIZE = "POLICY_VERSION_UPDATE_SIZE";

	public static String INCREMENT_MAIN_UPDATE = "INCREMENT_MAIN_UPDATE";
	public static String INCREMENT_SUB_UPDATE = "INCREMENT_SUB_UPDATE";
	public static String INCREMENT_UPDATE_SIZE = "INCREMENT_UPDATE_SIZE";   //增量包需要更新的当前包号
	public static String LOCAL_INCREMENT_MAIN_UPDATE = "LOCAL_INCREMENT_MAIN_UPDATE";
	public static String LOCAL_INCREMENT_SUB_UPDATE = "LOCAL_INCREMENT_SUB_UPDATE";

	public static String LOCAL_POLICY_VERSION_MAIN = "LOCAL_POLICY_VERSION_MAIN";// 本地正在更新的政策文件小版本
	public static String LOCAL_POLICY_VERSION_SUB = "LOCAL_POLICY_VERSION_SUB";// 本地正在更新的政策文件小版本

	public static String TIME = "TIME";

	public static String PSAM = "PSAM";
	public static String RFSAM = "RFSAM";

	public static String PASTTIME = "PASTTIME";
	public static String CHECK_HEARTBEAT = "CHECK_HEARTBEAT";

	public static String LONGTITUDE = "LONGTITUDE";
	public static String LATITUDE = "LATITUDE";
	public static String CITY = "CITY";
	public static String DISTRICT = "DISTRICT";
	public static String STREET = "STREET";
	public static String ADDR = "ADDR";
	public static String ORG = "ORG";
	public static String DEPARTMENT = "DEPARTMENT";

	// 是否为root设备
	public static String SH_TYPE = "SH_TYPE";

	// 升级yuga so文件
	public static String SOCOPYLIB = "SOCOPYLIB";
	// 是否为root设备
	public static String ROOT_STATUS = "ROOT_STATUS";

	private SharedPreferences mSharedPreferences;

	private LocalDataFactory(Context context) {
		mSharedPreferences = context.getSharedPreferences(DATA_PATH,
				Context.MODE_PRIVATE);
	}

	public static LocalDataFactory mInstance;

	public static LocalDataFactory newInstance(Context context) {
		if (mInstance == null) {
			mInstance = new LocalDataFactory(context);
		}
		return mInstance;
	}

	public void putString(String key, String value) {
		Editor editor = mSharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}

	public void putInt(String key, int value) {
		Editor editor = mSharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public int getInt(String key, int defValue) {
		return mSharedPreferences.getInt(key, defValue);
	}

	public void putLong(String key, Long value) {
		Editor editor = mSharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public void putDouble(String key, Double value) {
		Editor editor = mSharedPreferences.edit();
		editor.putLong(key, Double.doubleToRawLongBits(value));
		editor.commit();
	}

	public Double getDouble(String key, Double defValue) {
		return Double.longBitsToDouble(mSharedPreferences.getLong(key,
				Double.doubleToLongBits(defValue)));
	}

	public Long getLong(String key, long defValue) {
		return mSharedPreferences.getLong(key, defValue);
	}

	public void putBoolean(String key, boolean isAccess) {
		Editor editor = mSharedPreferences.edit();
		editor.putBoolean(key, isAccess);
		editor.commit();
	}

	public Boolean getBoolean(String key, boolean defValue) {
		return mSharedPreferences.getBoolean(key, defValue);
	}
}
