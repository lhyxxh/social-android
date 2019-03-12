package com.eastcom.social.pos.service;

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;

import com.eastcom.social.pos.core.orm.entity.CommandConfirm;
import com.eastcom.social.pos.core.service.CommandConfirmService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.confirm.ConfirmMssage;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.receiver.AlarmReceiver;
import com.eastcom.social.pos.util.MyLog;

/**
 * 上传app版本
 * 
 * @author eronc
 *
 */
public class FollowCommandConfirmService extends Service {

	private CommandConfirmService commandConfirmService;
	
	private GetDataTask getDataTask;

	private SoClient client;

	@Override
	public void onCreate() {
		super.onCreate();
		client = MySoClient.newInstance().getClient();
		commandConfirmService = CommandConfirmService.getInstance(this);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startGetDataTask();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 1 * 60 * 60 * 1000;
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AlarmReceiver.class);
		i.setAction("COMMANDCONFIRM");
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
				CommandConfirm();
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

	private void CommandConfirm() {
		List<CommandConfirm> list =commandConfirmService.loadAllCommandConfirm();
		for (int i = 0; i < list.size(); i++) {
			try {
				final CommandConfirm commandConfirm = list.get(i);
				int followCommand = commandConfirm.getType();
				client.sendMessage(new ConfirmMssage(
						followCommand, 1),
						new ActivityCallBackListener() {

							@Override
							public void doTimeOut() {
								MyLog.i("ConfirmMssageService", "ConfirmRfsamList time out");
							}

							@Override
							public void callBack(
									SoMessage message) {
								MyLog.i("ConfirmMssageService", "ConfirmRfsamList");
								commandConfirmService.deleteCommandConfirm(commandConfirm.getID());
							}
						});
			} catch (Exception e) {
				MyLog.e("FollowCommandConfirmService", "error:"+e.getMessage());
			}
		}
	}


}
