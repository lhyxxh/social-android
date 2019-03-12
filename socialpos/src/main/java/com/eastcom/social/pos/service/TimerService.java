package com.eastcom.social.pos.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.systime.SysTimeMessage;
import com.eastcom.social.pos.core.socket.message.systime.SysTimeRespMessage;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.receiver.DateTimeChangeReciever;
import com.eastcom.social.pos.util.MyLog;
import com.eastcom.social.pos.util.SignBoardUtils;

public class TimerService extends Service {

	private GetDataTask getDataTask;
	private SoClient client;

	private Timer timer = null;// 计时器
	private TimerTask timerTask = null;

	@Override
	public void onCreate() {
		client = MySoClient.newInstance().getClient();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startGetDataTask();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopTime();
	}

	public void startGetDataTask() {
		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute((Void) null);
		}
	}

	public class GetDataTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			startTime();
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			getDataTask = null;
		}

	}

	/**
	 * 获取系统时间
	 */
	private void getSysTime() {
		MyApplicationLike.stopTimeConsumingService(false);
		SysTimeMessage sysTimeMessage = new SysTimeMessage();
		MyLog.i("MySoClient", "getSysTime");
		if (client != null) {
			client.sendMessage(sysTimeMessage, new ActivityCallBackListener() {

				@Override
				public void doTimeOut() {
					MyLog.i("MySoClient", "getSysTime timeout");
					loop ++;
					if (loop >5) {
						stopTime();
					}
				}

				@Override
				public void callBack(SoMessage message) {
					MyLog.i("MySoClient", "getSysTime callBack ");
					SysTimeRespMessage sysTimeRespMessage = new SysTimeRespMessage(
							message);
					String sysTime = sysTimeRespMessage.getSysTime();
					MyLog.i("MySoClient", "getSysTime = " + sysTime);
					DateTimeChangeReciever.flagTimeService = true;
					SignBoardUtils.changeSystemTime(sysTime);
					//关闭任务
					stopTime();
				}
			}, 5);
		}
	
	}
	
	private int loop = 1;

	/**
	 * 开始自动计时
	 */
	private void startTime() {
		loop = 1;
		if (timer == null) {
			timer = new Timer();
		}

		timerTask = new TimerTask() {

			@Override
			public void run() {
				// 执行获取时间任务
				getSysTime();

			}
		};
		timer.schedule(timerTask, 0, 10000);// 1000ms执行一次
	}

	/**
	 * 停止自动计时
	 */
	private void stopTime() {
		if (timer != null)
			timer.cancel();
	}

}
