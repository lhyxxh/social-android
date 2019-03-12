package com.eastcom.social.pos.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class AppListEntity {
	private String appId;
	private String packageName;
	private String appName;
	private String appRemark;
	private String versionName;
	private int versionCode;
	private int uploadLogo;
	
	private int layoutType = 0;

	public String getId() {
		return appId;
	}

	public void setId(String appId) {
		this.appId = appId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppRemark() {
		return appRemark;
	}

	public void setAppRemark(String appRemark) {
		this.appRemark = appRemark;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getUploadLogo() {
		return uploadLogo;
	}

	public void setUploadLogo(int uploadLogo) {
		this.uploadLogo = uploadLogo;
	}

	public void setAppListEntity(JSONArray jsonArray,ArrayList<AppListEntity> list) {

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = jsonArray.optJSONObject(i);
			AppListEntity entity = new AppListEntity();
			entity.setId(json.optString("id"));
			entity.setPackageName(json.optString("packageName"));//文档中是package，不是packageName
			entity.setAppName(json.optString("name"));
			entity.setAppRemark(json.optString("remark"));
			entity.setVersionName(json.optString("versionName"));
			entity.setVersionCode(json.optInt("versionCode"));
			entity.setUploadLogo(json.optInt("uploadLogo"));
			list.add(entity);
		}
	}
	
}
