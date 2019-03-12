package com.eastcom.social.pos.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.service.LocalDataFactory;

public class VersionUtil {
	public static String getVersion(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return context.getString(R.string.version_name) + LocalDataFactory.LocalMainVersion+"."+LocalDataFactory.LocalSubVersion;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
