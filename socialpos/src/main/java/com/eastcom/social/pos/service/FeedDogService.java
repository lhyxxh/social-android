package com.eastcom.social.pos.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.eastcom.social.pos.receiver.AlarmReceiver;

/**
 * 喂狗服务
 * @author eronc
 *
 */
public class FeedDogService extends Service {

	private GetDataTask getDataTask;

	@Override
	public void onCreate() {
		super.onCreate();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startGetDataTask();
		int anHour = 5 * 1000;
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent i = new Intent(this, AlarmReceiver.class);
		i.setAction("FEEDDOG");
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
				FeedDog();
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

	private void FeedDog() {
		Intent intent = new Intent("com.ubox.watchdog.feed");
		intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		sendBroadcast(intent);
		Log.i("----------------------", "喂狗成功");
	}

}
