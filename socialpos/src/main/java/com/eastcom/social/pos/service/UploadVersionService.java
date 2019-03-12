package com.eastcom.social.pos.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.checkversion.CheckVersionMessage;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.util.MyLog;

/**
 * 自动更新服务
 * 
 * @author eronc
 *
 */
public class UploadVersionService extends Service {

	private GetDataTask getDataTask;

	private SoClient client;

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
				checkVersion();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

	}

	
	

	private void checkVersion() {
		try {
			String versionName = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
			final int versionMain = Integer
					.valueOf(versionName.split("\\.")[0]);
			final int versionSub = Integer.valueOf(versionName.split("\\.")[1]);
			CheckVersionMessage checkVersionMessage = new CheckVersionMessage(
					versionMain, versionSub);
			client.sendMessage(checkVersionMessage,
					new ActivityCallBackListener() {

						@Override
						public void doTimeOut() {
							MyLog.i("UploadVersionService",
									"CheckVersionMessage doTimeOut");
							stopSelf();
						}

						@Override
						public void callBack(SoMessage message) {
							MyLog.i("UploadVersionService",
									"CheckVersionMessage callBack");
							stopSelf();
						}
					}, 10);
		} catch (Exception e) {
			MyLog.i("UploadVersionService", "CheckVersionMessage Exception");
			stopSelf();
		}

	}

}
