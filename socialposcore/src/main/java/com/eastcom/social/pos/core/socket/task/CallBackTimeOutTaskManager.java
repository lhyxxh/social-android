package com.eastcom.social.pos.core.socket.task;

import java.util.Timer;

public class CallBackTimeOutTaskManager {

	/**
	 * 
	 * @param loopSecond
	 *            循环检测时间间隔
	 * @param timeSecond
	 *            回调超时判断标准
	 * @param clear
	 *            是否只执行一次超时回调
	 */
	public CallBackTimeOutTaskManager(int loopSecond, int timeSecond, boolean clear) {
		Timer timer = new Timer();
		CallBackTimeOutTask task = new CallBackTimeOutTask(timeSecond, clear);
		timer.schedule(task, 0, loopSecond * 1000);
	}

}