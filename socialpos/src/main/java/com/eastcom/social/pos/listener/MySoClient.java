package com.eastcom.social.pos.listener;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.config.Constance;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.client.SoClientInitializer;
import com.eastcom.social.pos.core.socket.client.mylistener.CheckIncrementVersion.MyCheckIncrementVersionRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.ClientChannelInitedListener;
import com.eastcom.social.pos.core.socket.client.mylistener.ClientMessageRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.alarm.MyAlarmRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.checkversion.MyCheckVersionRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.confirm.MyConfirmRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.encrypt.MyEncryptRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.healthproduct.MyHealthProductRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.medicineproduct.MyMedicineProductRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.queryhealth.MyQueryHealthRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.querymedicine.MyQueryMedicineRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.setheartbeat.MySetHeartBeatRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.socialinfo.MySocialInfoRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.socketurl.MySocketUrlRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.summitcardlog.MySummitCardlogRespListener;
import com.eastcom.social.pos.core.socket.client.mylistener.updatesoft.MyUpdateSoftRespListener;
import com.eastcom.social.pos.core.socket.config.ClientConfig;
import com.eastcom.social.pos.core.socket.listener.ClientRunningListener;
import com.eastcom.social.pos.core.socket.task.CallBackTimeOutTaskManager;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.util.MyLog;

/**
 * socket心跳客户端实例
 * 
 * @author eronc
 *
 */
public class MySoClient {
	public static MySoClient mInstance;

	public SoClient client;

	public static MySoClient newInstance() {
		if (mInstance == null) {
			MyLog.i("MySoClient", "MySoClient1");
			mInstance = new MySoClient();
		}
		return mInstance;
	}

	public MySoClient() {
		// host 及 eid 改为保存本地SharedPreferences 响应后续指令
		MyLog.i("MySoClient", "MySoClient");
		LocalDataFactory localDataFactory = LocalDataFactory
				.newInstance(MyApplicationLike.getContext());
		String host = localDataFactory.getString(LocalDataFactory.HOST, "");
		int port = localDataFactory.getInt(LocalDataFactory.POST, 0);
		String eid = localDataFactory.getString(LocalDataFactory.EID, "");
		if ("".equals(host)) {
			host = Constance.host;
		}
		if (port == 0) {
			port = Constance.port;
		}
		ClientConfig config = new ClientConfig(host, port, eid);
		client = new SoClient(config);
	}
	
	public void run() {
		// host 及 eid 改为保存本地SharedPreferences 响应后续指令
		MyLog.i("MySoClient", "run");
		ClientRun();
	}

	private void ClientRun() {
		// 配置通道初始化器
		SoClientInitializer initializer = new SoClientInitializer();
		// 基本通道加载类，必填
		ClientChannelInitedListener clientChannelInitedListener = new ClientChannelInitedListener(client);
		initializer.setChannelInitedListener(clientChannelInitedListener);
		// 标牌心跳频率和超时检测频率，选填，默认为60，10
			initializer.setHeartbeatRate(60);

		// 指令返回值处理通道，选填
			ClientMessageRespListener clientMessageRespListener = new ClientMessageRespListener();
		initializer.setMessageRespListener(clientMessageRespListener);
		initializer
				.setCheckVersionRespListener(new MyCheckVersionRespListener());
		initializer.setConfirmRespListener(new MyConfirmRespListener());
		initializer
				.setHealthProductRespListener(new MyHealthProductRespListener());
		initializer.setHeartbeatRespListener(new MyHeartbeatRespListener());
		initializer
				.setMedicineProductRespListener(new MyMedicineProductRespListener());
		initializer.setQueryHealthRespListener(new MyQueryHealthRespListener());
		initializer
				.setQueryMedicineRespListener(new MyQueryMedicineRespListener());
		initializer.setTradeRespListener(new MyTradeRespListener());
		initializer.setTradeDetailRespListener(new MyTradeDetailRespListener());
		initializer.setUpdateSoftRespListener(new MyUpdateSoftRespListener());
		initializer.setRfsamListRespListener(new MyRfsamListRespListener());
		initializer
				.setRfsamValidTimeRespListener(new MyRfsamvalidTimeRespListener());

		initializer.setAlarmRespListener(new MyAlarmRespListener());
		initializer.setSocialInfoRespListener(new MySocialInfoRespListener());

		initializer.setSummitCardLogRespListener(new MySummitCardlogRespListener());
		initializer.setSocketUrlRespListener(new MySocketUrlRespListener());
		initializer.setEncryptRespListener(new MyEncryptRespListener());
		initializer
				.setSetHeatbeatRespListener(new MySetHeartBeatRespListener());

		initializer.setNoDataRespListener(new MyNoDataRespListener());
		initializer.setCheckIncreamentVersionRespListener(new MyCheckIncrementVersionRespListener());
		client.setInitializer(initializer);
		new CallBackTimeOutTaskManager(2, 10, false);
		new Thread() {
			public void run() {
				try {
					client.run(new ClientRunningListener() {

						@Override
						public void afterRunning() {
							MyLog.w("SocketClient", "tcp is start \n");

						}

						@Override
						public void clientReStart() {
							MyLog.w("SocketClient", "tcp is restarting \n");
						}

						@Override
						public void clientClosing() {
							MyLog.w("SocketClient", "SocketClient is closing\n");
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public SoClient getClient() {
		return client;
	}

	/**
	 * 重新配置client
	 * 
	 * @param client
	 * @param host
	 * @param port
	 * @param eid
	 */
	public void setClient(String host, int port, String eid) {
		this.client.stop();
		ClientConfig config = new ClientConfig(host, port, eid);
		SoClient client = new SoClient(config);
		this.client = client;
		ClientRun();
	}

	/**
	 * 关闭闹钟
	 */
	public static void closeAlarm() {
		MyLog.i("MySoclient", "closeAlarm");
		MyApplicationLike.getContext().stopService(MyApplicationLike.updateSoftService);
		MyApplicationLike.getContext().stopService(
				MyApplicationLike.uploadVersionService);
		MyApplicationLike.getContext().stopService(MyApplicationLike.uploadService);
		MyApplicationLike.getContext().stopService(MyApplicationLike.updateService);
		MyApplicationLike.getContext().stopService(MyApplicationLike.slowUplodeService);
		MyApplicationLike.getContext().stopService(MyApplicationLike.gprsService);
		MyApplicationLike.getContext().stopService(MyApplicationLike.policyService);
		MyApplicationLike.getContext().stopService(MyApplicationLike.timerService);
		MyApplicationLike.getContext().stopService(
				MyApplicationLike.queryMedicineService);

		AlarmManager alarm = (AlarmManager) MyApplicationLike.getContext()
				.getSystemService(Activity.ALARM_SERVICE);

//		PendingIntent pi_updateSoft = PendingIntent.getService(
//				MyApplicationLike.getContext(), 0, MyApplicationLike.updateSoftService,
//				0);
//		alarm.cancel(pi_updateSoft);
		PendingIntent pi_updateSoft = PendingIntent.getService(
				MyApplicationLike.getContext(), 0, MyApplicationLike.updateIncrementService,
				0);
		alarm.cancel(pi_updateSoft);

		PendingIntent pendingIntent = PendingIntent.getService(
				MyApplicationLike.getContext(), 0, MyApplicationLike.uploadService, 0);
		alarm.cancel(pendingIntent);

		PendingIntent pi_update = PendingIntent.getService(
				MyApplicationLike.getContext(), 0, MyApplicationLike.updateService, 0);
		alarm.cancel(pi_update);

		PendingIntent pi_slowUpload = PendingIntent.getService(
				MyApplicationLike.getContext(), 0, MyApplicationLike.slowUplodeService,
				0);
		alarm.cancel(pi_slowUpload);

		PendingIntent pi_gprs = PendingIntent.getService(
				MyApplicationLike.getContext(), 0, MyApplicationLike.gprsService, 0);
		alarm.cancel(pi_gprs);

		PendingIntent pi_queryMedicine = PendingIntent.getService(
				MyApplicationLike.getContext(), 0,
				MyApplicationLike.queryMedicineService, 0);
		alarm.cancel(pi_queryMedicine);

		PendingIntent pi_uploadVersion = PendingIntent.getService(
				MyApplicationLike.getContext(), 0,
				MyApplicationLike.uploadVersionService, 0);
		alarm.cancel(pi_uploadVersion);
		
		PendingIntent pi_uploadTradeFile = PendingIntent.getService(
				MyApplicationLike.getContext(), 0,
				MyApplicationLike.uploadTradeFileService, 0);
		alarm.cancel(pi_uploadTradeFile);

	}

}
