package com.eastcom.social.pos.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetWorkUtil {
	/**
	 * ping 指定ip
	 * 
	 * @param ip
	 * @return
	 */
	public static int pingUrl(String ip) {
		int result = 0;
		try {
			Process p = Runtime.getRuntime().exec("ping -c 3 -w 3 " + ip);
			result = p.waitFor();
		} catch (Exception e) {
			result = 100;
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 判断是否有网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断网络类型
	 * 
	 * @param context
	 * @return
	 */
	public static int GetNetype(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = 3;
			} else {
				netType = 2;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 1;
		}
		return netType;
	}

	/**
	 * 获取网络异常原因类型
	 * 
	 * @param context
	 * @return
	 */
	public static int getNetWorkErrorType(Context context) {
		int type = 0;
		if (!getSimState(context)) {
			type = 1;
		}else if (!isNetworkConnected(context)) {
			type = 2;
		}else if (!getMccAndMnc(context)) {
			type = 3;
		}
		return type;
	}

	/**
	 * 获取sim卡状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getSimState(Context context) {
		boolean flag = false;
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);// 取得相关系统服务
		switch (tm.getSimState()) { // getSimState()取得sim的状态 有下面6中状态
		case TelephonyManager.SIM_STATE_ABSENT:
		case TelephonyManager.SIM_STATE_UNKNOWN:
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
		case TelephonyManager.SIM_STATE_PIN_REQUIRED:
		case TelephonyManager.SIM_STATE_PUK_REQUIRED:
			flag = false;
			break;

		case TelephonyManager.SIM_STATE_READY:
			flag = true;
			break;

		}
		return flag;
	}
	
	/**
	 * 获取MCC和MNC
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getMccAndMnc(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);// 取得相关系统服务
		String simOperator = tm.getSimOperator();
		if (simOperator == null||"".equals(simOperator)) {
			return false;
		}else {
			return true;
		}
	}
}
