package com.eastcom.social.pos.entity;

import java.util.ArrayList;

import org.json.JSONObject;

public class AppDetailEntity {
	private String versionRemark;
	
	private Long fileSize;
	private int updateDate;
	private String versionName;
	private int versionCode;
	private int uploadPhoto;
	
	public String getVersionRemark() {
		return versionRemark;
	}
	public void setVersionRemark(String versionRemark) {
		this.versionRemark = versionRemark;
	}
	
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	public int getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(int updateDate) {
		this.updateDate = updateDate;
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
	
	public int getUploadPhoto() {
		return uploadPhoto;
	}
	public void setUploadPhoto(int uploadPhoto) {
		this.uploadPhoto = uploadPhoto;
	}
	
	
	public void setAppDetailEntity(JSONObject json, ArrayList<AppDetailEntity> list) {
		
			AppDetailEntity entity = new AppDetailEntity();
			entity.setVersionRemark(json.optString("versionRemark"));
			entity.setFileSize(json.optLong("fileSize"));
			entity.setUpdateDate(json.optInt("updateDate"));
			entity.setVersionName(json.optString("versionName"));
			entity.setVersionCode(json.optInt("versionCode"));
			entity.setUploadPhoto(json.optInt("uploadPhoto"));
			list.add(entity);
	}
}
