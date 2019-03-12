package com.eastcom.social.pos.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;

import com.eastcom.social.pos.entity.DeviceAppInfoEntity;

public class QueryDeviceAppInfo {
	/**
	 * 查询当前设备所安装的所有APP应用，根据包名判断是否已经安装了该应用程序
	 * 
	 */
	// 查看设备各APP信息版本号
	private static ArrayList<DeviceAppInfoEntity> mlistAppInfo = null;// 本地APP信息

	public static ArrayList<DeviceAppInfoEntity> queryDeviceAppInfo(Context context,String pkgName) {
		mlistAppInfo = new ArrayList<DeviceAppInfoEntity>();
		// 可获取到每个应用的版本号；但是不能获取到每个应用的启动Activity，也就是不能通过点击跳转到相应的应用程序
		try {
			List<PackageInfo> packages = context.getPackageManager()
					.getInstalledPackages(0);
			for (int i = 0; i < packages.size(); i++) {
				PackageInfo packageInfo = packages.get(i);
				DeviceAppInfoEntity tmpInfo = new DeviceAppInfoEntity();
				tmpInfo.setPkgName(packageInfo.packageName);
				tmpInfo.setVersionName(packageInfo.versionName);
				tmpInfo.setVersionCode(packageInfo.versionCode);
				tmpInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(context
						.getPackageManager()));
				tmpInfo.setAppLabel(packageInfo.applicationInfo.loadLabel(
						context.getPackageManager()).toString());
				mlistAppInfo.add(tmpInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("MainActivity", "错误是------>>>>>" + e.toString());
		}
		return mlistAppInfo;

	}
	

}