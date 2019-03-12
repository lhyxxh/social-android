package com.eastcom.social.pos.core.socket.model;

import java.util.Date;

import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;

/**
 * 全局监听回调模型
 */
public class ActivityCallBackModel {

	private Date createDate;

	private Date lastExecuteDate;

	private boolean doCallBack;

	private boolean doCallBackTimeOut;

	private long timeOut;

	private ActivityCallBackListener listener;

	public ActivityCallBackModel(ActivityCallBackListener listener) {
		this.listener = listener;
		this.createDate = new Date();
		this.doCallBack = true;
		this.doCallBackTimeOut = true;
		this.timeOut = 10;
	}

	public ActivityCallBackModel(ActivityCallBackListener listener, int timeOut) {
		this.listener = listener;
		this.createDate = new Date();
		this.doCallBack = true;
		this.doCallBackTimeOut = true;
		this.timeOut = timeOut;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastExecuteDate() {
		return lastExecuteDate;
	}

	public void setLastExecuteDate(Date lastExecuteDate) {
		this.lastExecuteDate = lastExecuteDate;
	}

	public boolean isDoCallBack() {
		return doCallBack;
	}

	public void setDoCallBack(boolean doCallBack) {
		this.doCallBack = doCallBack;
	}

	public boolean isDoCallBackTimeOut() {
		return doCallBackTimeOut;
	}

	public void setDoCallBackTimeOut(boolean doCallBackTimeOut) {
		this.doCallBackTimeOut = doCallBackTimeOut;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public ActivityCallBackListener getListener() {
		return listener;
	}

	public void setListener(ActivityCallBackListener listener) {
		this.listener = listener;
	}

}
