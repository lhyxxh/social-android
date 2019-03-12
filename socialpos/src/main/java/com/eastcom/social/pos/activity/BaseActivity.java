package com.eastcom.social.pos.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.eastcom.social.pos.config.Constance;
import com.eastcom.social.pos.core.orm.entity.Document;
import com.eastcom.social.pos.core.service.DocumentService;
import com.eastcom.social.pos.service.LocalDataFactory;
import com.eastcom.social.pos.util.MyLog;
import com.example.usbcciddrv.jniUsbCcid;

public abstract class BaseActivity extends Activity {
	private MyBroadcastReceiver mBroadcastReceiver;

	private DocumentService documentService;
	private ArrayList<String> mFileNmaes = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constance.BROADCAST_ACTION);
		intentFilter.addAction(Constance.PING_STATUS);
		intentFilter.addAction(Constance.RFSAM_STATUS);
		intentFilter.addAction(Constance.CLOCK_ACTION);
		registerReceiver(mBroadcastReceiver, intentFilter);
		documentService = DocumentService.getInstance(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBroadcastReceiver != null) {
			unregisterReceiver(mBroadcastReceiver);
		}

	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!BaseActivity.this.getClass().equals(ReadPDFActivity.class)) {
			List<Document> list = documentService.loadDocumentNotRead();
			if (list.size() > 0) {
				MyLog.i("BaseActivity", "list size = " +list.size());
				for (int i = 0; i < list.size(); i++) {
					mFileNmaes.add(list.get(i).getFileName());
				}
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("pathList", mFileNmaes);
				intent.putExtras(bundle);
				intent.setClass(BaseActivity.this, ReadPDFActivity.class);
				startActivity(intent);
			}
		}

	}

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public boolean isAppOnForeground() {
		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Constance.BROADCAST_ACTION)) {
				boolean NODATA = intent.getBooleanExtra("nodata", false);
				Nodata(NODATA);
			} else if (intent.getAction().equals(Constance.RFSAM_STATUS)) {
				int status = intent.getIntExtra("rfsam_status", 0);
				getRfsamStatus(status);
			} else if (intent.getAction().equals(Constance.CLOCK_ACTION)) {
				String time = intent.getStringExtra("time");
				setTime(time);
			} else if (intent.getAction().equals(Constance.PING_STATUS)) {
				boolean status = intent.getBooleanExtra("status", false);
				setPingStatus(status);
			}

		}

	}

	/**
	 * 检测设备是否绑定
	 */
	public abstract void Nodata(boolean nodata);

	/**
	 * 检测设备是否停用
	 * 
	 * @param status
	 */
	public void getRfsamStatus(int status) {
		if (status == 1) {
			LocalDataFactory localDataFactory = LocalDataFactory
					.newInstance(BaseActivity.this);
			jniUsbCcid.SetTimer(10);
			localDataFactory.putInt(LocalDataFactory.RFSAM_STATUS, 1);
			jniUsbCcid.Eb_State(2, 0, 2);
		} else {
			LocalDataFactory localDataFactory = LocalDataFactory
					.newInstance(BaseActivity.this);
			localDataFactory.putInt(LocalDataFactory.RFSAM_STATUS, 2);
			if (!BaseActivity.this.getClass().equals(MainActivity.class)
					&& !BaseActivity.this.getClass().equals(
							PolicyWordActivity.class)
					&& !BaseActivity.this.getClass().equals(
							NewConsumeActivity.class)
					&& !BaseActivity.this.getClass()
							.equals(ChartActivity.class)
					&& !BaseActivity.this.getClass().equals(
							AppStoreActivity.class)
					&& !BaseActivity.this.getClass().equals(
							SettingActivity.class)) {
				finish();
			}
			jniUsbCcid.Eb_State(2, 0, 1);
		}
	}

	public void setPingStatus(boolean status) {

	}

	/**
	 * 获取并设置系统时间
	 */
	public abstract void setTime(String time);
}
