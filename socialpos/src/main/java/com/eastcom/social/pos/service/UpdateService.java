package com.eastcom.social.pos.service;

import java.io.File;
import java.util.List;

import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.util.dialog.CustomDialog;
import com.eastcom.social.pos.util.dialog.LoadingDialog;

/**
 * 自动更新服务
 * 
 * @author eronc
 *
 */
public class UpdateService extends Service {

	private GetDataTask getDataTask;
	private int operateType = 0;
	public LoadingDialog loadingdialog;
	private String eid;
	private AlertDialog.Builder builder;
	private AlertDialog alertDialog;
	private CustomDialog dialog;
	private String version;

	@Override
	public void onCreate() {
		loadingdialog = new LoadingDialog(this, "系统正在升级,请稍等");
		loadingdialog.setCanceledOnTouchOutside(false);
		builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
		LocalDataFactory localDataFactory = LocalDataFactory
				.newInstance(MyApplicationLike.getContext());
		eid = localDataFactory.getString(LocalDataFactory.EID, "");
		super.onCreate();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		operateType = 0;
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

				if (operateType == 0) {
					String localVersionName = getPackageManager()
							.getPackageInfo(getPackageName(), 0).versionName;
					JSONObject json = new JSONObject(DataFactory.postVersion(
							eid, localVersionName));
					if ("true".equals(json.getString("success"))) {
						version = json.getString("item");
						return true;
					} else {
						return false;
					}

				} else if (operateType == 1) {
					Message message0 = new Message();
					message0.what = 0;
					mHandler2.sendMessage(message0);
					String filePath = DataFactory.postUpGrade(version);
					Message message1 = new Message();
					message1.what = 1;
					mHandler2.sendMessage(message1);
					if (!"error".equals(filePath)) {
						installApk(filePath);
					} else {
						Message message3 = new Message();
						message3.what = 3;
						mHandler2.sendMessage(message3);
					}

				}

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
			try {
				if (result) {
					if (operateType == 0) {
						String localVersionName = getPackageManager()
								.getPackageInfo(getPackageName(), 0).versionName;
						if (!localVersionName.equals(version)) {
							operateType = 1;
							sendOpenBroadcast();
							startGetDataTask();
							loadingDialogShow();

						} else {
						}
					} else if (operateType == 1) {

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void showAlertDialog() {
		Log.e("-------", "update");
		CustomDialog.Builder builder = new CustomDialog.Builder(this);
		builder.setTitle("软件版本更新");
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				operateType = 1;
				sendOpenBroadcast();
				startGetDataTask();
				loadingDialogShow();
			}

		});

		builder.setNegativeButton("以后再说",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						loadingDialogCancle();
					}
				});
		dialog = builder.create();
		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();

		Log.e("-------", "update111");
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk(String saveFileName) {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			Message message3 = new Message();
			message3.what = 4;
			mHandler2.sendMessage(message3);
			return;
		}

		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		startActivity(i);
	}

	public Handler mHandler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				loadingDialogShow();
				break;
			case 1:
				loadingDialogCancle();
				break;
			case 2:
				loadingDialogCancle();
				break;
			case 3:
				AlertDialog("下载失败");
				sendCloseBroadcast();
				break;
			case 4:
				AlertDialog("安装失败");
				sendCloseBroadcast();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void AlertDialog(String title) {
		try {
			builder.setTitle("温馨提示");
			builder.setMessage(title);
			builder.setPositiveButton("确定", null);
			if (alertDialog == null) {
				alertDialog = builder.create();
				alertDialog.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				alertDialog.show();
			} else {
				if (!alertDialog.isShowing()) {
					alertDialog.show();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadingDialogShow() {
		loadingdialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		loadingdialog.show();
	}

	private void loadingDialogCancle() {
		loadingdialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		loadingdialog.cancel();
	}


	/**
	 * 发送 打开未知来源选项广播
	 */
	private void sendOpenBroadcast() {
		Intent intent = new Intent();
		intent.setAction("android.intent.for.install.y");
		sendBroadcast(intent);
	}

	/**
	 * 发送 关闭未知来源选项广播
	 */
	private void sendCloseBroadcast() {
		Intent intent = new Intent();
		intent.setAction("android.intent.for.install.n");
		sendBroadcast(intent);
	}
	

	/** 
     * 程序是否在前台运行 
     *  
     * @return 
     */  
    public boolean isAppOnForeground() {  
               
            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);  
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

}
