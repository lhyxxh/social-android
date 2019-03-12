package com.eastcom.social.pos.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.receiver.AlarmReceiver;
import com.eastcom.social.pos.util.MyLog;
import com.eastcom.social.pos.util.TimeUtil;
import com.example.usbcciddrv.jniUsbCcid;

/**
 * rfsam卡授权服务
 * 
 * @author eronc
 *
 */
public class AuthService extends Service {

	private GetDataTask getDataTask;
	private LocalDataFactory localDataFactory;

	@Override
	public void onCreate() {
		super.onCreate();
		 localDataFactory = LocalDataFactory
				.newInstance(MyApplicationLike.getContext());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		startGetDataTask();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 2 * 60 * 1000;
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AlarmReceiver.class);
		i.setAction("AUTH");
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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
			try {
				Auth();
//				checkHeartBeat();
			} catch (Exception e) {
				getDataTask = null;
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			getDataTask = null;
		}

	}

	/**
	 * 电子标牌授权
	 */
	private void Auth() {
		LocalDataFactory localDataFactory = LocalDataFactory
				.newInstance(MyApplicationLike.getContext());
		int rfsam_status = localDataFactory.getInt(LocalDataFactory.RFSAM_STATUS, 0);// rfsam卡状态
		if (rfsam_status != 2) {
			int i = jniUsbCcid.SetTimer(3);
			if (i == 0) {
				jniUsbCcid.Eb_State(2, 0, 2);
			} else {
				jniUsbCcid.Eb_State(2, 0, 1);
			}
		} else {
			MyLog.i("Auth", "Auth fail");
		}
	}

	/**
	 * 检查忙时闲时心跳
	 */
	private void checkHeartBeat() {
		boolean checkHeartbeat = localDataFactory.getBoolean(LocalDataFactory.CHECK_HEARTBEAT, false);
		int start = 0 * 60 + 00;
		int end = 6 * 60;
		boolean freeTime = TimeUtil.betweenTime(start, end);
		if (checkHeartbeat) {
			// 启用闲时取消心跳功能
			if (freeTime) {
				// 闲时取消心跳
//				client = MySoClient.newInstance().getClient();
//				boolean isConnect = client.isConnect();
//				if (isConnect) {
//					//关闭已连接客户端
//					client.stop();
					MyLog.e("AuthService", "stop Client");
//				}
			} else {
				MyLog.e("AuthService", "stop Client");
//				//忙时启用心跳
//				client = MySoClient.newInstance().getClient();
//				boolean isConnect = client.isConnect();
//				if (!isConnect) {
//					//重新连接
//					String host = localDataFactory.getHost();
//					int port = localDataFactory.getPost();
//					localDataFactory.setHostAndPost(host, port);
//					String eid = localDataFactory.getEid();
//					MySoClient.newInstance().setClient(host, port, eid);
//					MyLog.e("AuthService", "start Client");
//				}
			}
		} else {
			// 不启用闲时取消心跳功能
			MyLog.e("AuthService", "stop Client");
//			if (freeTime) {
//				client = MySoClient.newInstance().getClient();
//				boolean isConnect = client.isConnect();
//				if (!isConnect) {
//					//重新连接
//					String host = localDataFactory.getHost();
//					int port = localDataFactory.getPost();
//					localDataFactory.setHostAndPost(host, port);
//					String eid = localDataFactory.getEid();
//					MySoClient.newInstance().setClient(host, port, eid);
//					MyLog.e("AuthService", "start Client");
//				}
//			}
		}
	}
}
