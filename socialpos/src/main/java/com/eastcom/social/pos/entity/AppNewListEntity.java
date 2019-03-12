package com.eastcom.social.pos.entity;


public class AppNewListEntity extends AppListEntity{

	private int isInstalled;
	private int isNewVersion;
	private boolean isUploadLogo = false;
	
	public int getIsInstalled() {
		return isInstalled;
	}
	public void setIsInstalled(int isInstalled) {
		this.isInstalled = isInstalled;
	}
	
	public int getIsNewVersion() {
		return isNewVersion;
	}
	public void setIsNewVersion(int isNewVersion) {
		this.isNewVersion = isNewVersion;
	}
	
	public boolean getIsUploadLogo() {
		return isUploadLogo;
	}
	public void setIsUploadLogo(boolean isUploadLogo) {
		this.isUploadLogo = isUploadLogo;
	}
}
