package com.eastcom.social.pos.core.socket.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.model.ActivityCallBackModel;

import android.annotation.SuppressLint;

/**
 * 回调超时检测
 */
public class CallBackTimeOutTask extends TimerTask {

	@SuppressLint("UseSparseArrays")
	public volatile static Map<Integer, ActivityCallBackModel> map = new HashMap<Integer, ActivityCallBackModel>();

	private int timeSecond = 10;

	private boolean clear = false;

	/**
	 * 
	 * @param timeSecond
	 *            回调超时判断标准
	 * @param clear
	 *            一个消息是否最多执行一次回调
	 */
	public CallBackTimeOutTask(int timeSecond, boolean clear) {
		this.timeSecond = timeSecond;
		this.clear = clear;
	}

	@Override
	public void run() {
//		System.out.println(new Date());
		Map<Integer, ActivityCallBackModel> myMap = map;
		for (Integer key : myMap.keySet()) {
			Date now = new Date();
			Date createDate = myMap.get(key).getCreateDate();
			Date lastExecuteDate = myMap.get(key).getLastExecuteDate();
			long timeOut = myMap.get(key).getTimeOut() == 0 ? timeSecond : myMap.get(key).getTimeOut();
			if (lastExecuteDate == null || lastExecuteDate.before(createDate)) {
				long second = (now.getTime() - createDate.getTime()) / 1000;
				if (second > timeOut) {
					ClientHandlerBase.doTimeOut(key);
					if (clear) {
						map.remove(key);
					}
				}
			}
		}
	}

}
