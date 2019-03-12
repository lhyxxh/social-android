package com.eastcom.social.pos.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.activity.ReadPDFActivity;
import com.eastcom.social.pos.config.Constance;
import com.eastcom.social.pos.core.orm.entity.Document;
import com.eastcom.social.pos.core.service.DocumentService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.checkpolicy.CheckPolicyMessage;
import com.eastcom.social.pos.core.socket.message.checkpolicy.CheckPolicyRespMessage;
import com.eastcom.social.pos.core.socket.message.checkpolicyversion.CheckPolicyVersionMessage;
import com.eastcom.social.pos.core.socket.message.checkpolicyversion.CheckPolicyVersionRespMessage;
import com.eastcom.social.pos.core.socket.message.updatepolicy.UpdatePolicyMessage;
import com.eastcom.social.pos.core.socket.message.updatepolicy.UpdatePolicyRespMessage;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.util.FileUtils;
import com.eastcom.social.pos.util.MyLog;

/**
 * tcp政策文件下载
 * 
 * @author eronc
 *
 */
public class PolicyService extends Service {

	private GetDataTask getDataTask;
	private int operateType = 0;

	private ArrayList<String> mFileNmaes = new ArrayList<String>();

	private LocalDataFactory localDataFactory;

	private SoClient client;

	private String suffix = "";// 文件后缀
	private String name = "";// 文件名
	private int mDownloadinNo;
	private DocumentService documentService;

	@Override
	public void onCreate() {
		MyLog.i("Policy", "oncreate");
		localDataFactory = LocalDataFactory.newInstance(MyApplicationLike
				.getContext());
		client = MySoClient.newInstance().getClient();
		documentService = DocumentService.getInstance(this);
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
					checkPolicy();
				} else if (operateType == 1) {
					int policyVerionUpdateSize = localDataFactory.getInt(
							LocalDataFactory.POLICY_VERSION_UPDATE_SIZE, 0);
					int policyVersionMainUpdate = localDataFactory.getInt(
							LocalDataFactory.POLICY_VERSION_MAIN_UPDATE, 0);
					int policyVersionSubUpdate = localDataFactory.getInt(
							LocalDataFactory.POLICY_VERSION_SUB_UPDATE, 0);
					MyLog.i("PolicyService", "policyVerionUpdateSize :"
							+ policyVerionUpdateSize);
					mDownloadinNo = policyVerionUpdateSize;
					update(policyVersionMainUpdate, policyVersionSubUpdate,
							policyVerionUpdateSize);
				}

			} catch (Exception e) {
				MyLog.i("PolicyService", "Exception");
				getDataTask = null;
				stopSelf();
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			getDataTask = null;
			if (result) {
				try {
					if (operateType == 0) {

					} else if (operateType == 1) {

					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * 请求政策文件基本信息
	 */
	private void checkPolicy() {
		CheckPolicyMessage checkPolicyMessage = new CheckPolicyMessage();
		client.sendMessage(checkPolicyMessage, new ActivityCallBackListener() {

			@Override
			public void doTimeOut() {
				MyLog.i("PolicyService", "CheckPolicyMessage doTimeOut");
				stopSelf();
			}

			@Override
			public void callBack(SoMessage message) {
				CheckPolicyRespMessage checkPolicyRespMessage = new CheckPolicyRespMessage(
						message);
				// int suffix_type = checkPolicyRespMessage.getSuffix_type();
				// suffix = getSuffix(suffix_type);
				suffix = checkPolicyRespMessage.getSuffix();
				name = checkPolicyRespMessage.getName();
				int versionMain = checkPolicyRespMessage.getVersionMain();
				int versionSub = checkPolicyRespMessage.getVersionSub();
				int localPolicyVersionMain = localDataFactory.getInt(
						LocalDataFactory.LOCAL_POLICY_VERSION_MAIN, 0);
				int localPolicyVersionSub = localDataFactory.getInt(
						LocalDataFactory.LOCAL_POLICY_VERSION_SUB, 0);
				int policyVersionMainUpdate = localDataFactory.getInt(
						LocalDataFactory.POLICY_VERSION_MAIN_UPDATE, 0);
				int policyVersionSubUpdate = localDataFactory.getInt(
						LocalDataFactory.POLICY_VERSION_SUB_UPDATE, 0);
				MyLog.i("PolicyService", "localPolicyVersionMain:"
						+ localPolicyVersionMain + "localPolicyVersionSub:"
						+ localPolicyVersionSub);
				MyLog.i("PolicyService", "versionMain:" + versionMain
						+ "versionSub:" + versionSub);
				if (localPolicyVersionMain == versionMain
						&& localPolicyVersionSub == versionSub) {
					// 升级完成
					PolicyConfirm(versionMain, versionSub);

				} else {
					if (versionMain == policyVersionMainUpdate
							&& versionSub == policyVersionSubUpdate) {
						// 本地更新的版本与服务器一致
					} else {
						// 本地更新的版本与服务器不一致，修改本地升级版本
						localDataFactory.putInt(
								LocalDataFactory.POLICY_VERSION_MAIN_UPDATE,
								versionMain);
						localDataFactory.putInt(
								LocalDataFactory.POLICY_VERSION_SUB_UPDATE,
								versionSub);
						localDataFactory.putInt(
								LocalDataFactory.POLICY_VERSION_UPDATE_SIZE, 0);
					}
					operateType = 1;
					startGetDataTask();
				}

			}

			private String getSuffix(int suffix_type) {
				String suffix = "";
				switch (suffix_type) {
				case 1:
					suffix = "txt";
					break;
				case 2:
					suffix = "pdf";
					break;
				case 3:
					suffix = "word";
					break;
				default:
					break;
				}
				return suffix;
			}
		});
	}

	/**
	 * 请求政策文件数据包
	 * 
	 * @param versionMain
	 * @param versionSub
	 * @param packetNo
	 */
	private void update(final int versionMain, final int versionSub,
			int packetNo) {
		MyApplicationLike.stopTimeConsumingService(true);
		UpdatePolicyMessage updatePolicyMessage = new UpdatePolicyMessage(
				versionMain, versionSub, packetNo);
		client.sendMessage(updatePolicyMessage, new ActivityCallBackListener() {

			@Override
			public void doTimeOut() {
				MyLog.i("PolicyService", "UpdatePolicyMessage doTimeOut");
				stopSelf();
			}

			@Override
			public void callBack(SoMessage message) {
				try {
					UpdatePolicyRespMessage updatePolicyRespMessage = new UpdatePolicyRespMessage(
							message);
					int versionFlag = updatePolicyRespMessage.getVersionFlag();
					int versionSize = updatePolicyRespMessage.getVersionSize();
					byte[] data = updatePolicyRespMessage.getData();
					String path = versionMain + "_" + versionSub + "/";
					String tempFileName = "" + versionFlag;
					File word = new File(Constance.PolicyFolderPath);
					if (!word.exists()) {
						word.mkdir();
					}
					File dir = new File(Constance.PolicyFolderPath + path);
					if (!dir.exists()) {
						dir.mkdir();
					}
					File file = new File(Constance.PolicyFolderPath + path,
							tempFileName);
					FileUtils.createFileWithByte(file, data);
					if (versionFlag + 1 >= versionSize) {
						MyLog.i("PolicyService", "download last flag is done");

						// 合并文件
						String fileName = name + "." + suffix;
						mergeFile(fileName, versionSize, path);
						String realFilePath = Constance.PolicyFolderPath
								+ fileName;
						File realFile = new File(realFilePath);
						if (!realFile.exists()) {
							MyLog.i("PolicyService", "download file not exist");
							localDataFactory
									.putInt(LocalDataFactory.POLICY_VERSION_UPDATE_SIZE,
											0);
							mDownloadinNo = 0;
						} else {
							localDataFactory.putInt(
									LocalDataFactory.LOCAL_POLICY_VERSION_MAIN,
									versionMain);
							localDataFactory.putInt(
									LocalDataFactory.LOCAL_POLICY_VERSION_SUB,
									versionSub);
							localDataFactory
									.putInt(LocalDataFactory.POLICY_VERSION_UPDATE_SIZE,
											0);
							mDownloadinNo = 0;
							// 删除临时文件夹
//							FileUtils
//									.deleteDirectory(Constance.PolicyFolderPath
//											+ path);
							Intent intent = new Intent();
							intent.setAction("POLICY");
							intent.putExtra("flag", "1");
							intent.putExtra("fileName", fileName);
							MyApplicationLike.getContext().sendBroadcast(intent);
							// 增加未阅读政策文件记录
							Document document = new Document(UUID.randomUUID()
									.toString(), fileName, "", 1, "", 1, 0, "",
									"", "");
							documentService.savDocument(document);
							Skip(fileName);
							// 确认政策文件版本下载完成
							PolicyConfirm(versionMain, versionSub);
						}
						stopSelf();
					} else {
						if (mDownloadinNo == versionFlag) {
							// 正在下载的包号与返回一致
							localDataFactory
									.putInt(LocalDataFactory.POLICY_VERSION_UPDATE_SIZE,
											versionFlag);
							mDownloadinNo = versionFlag + 1;
							update(versionMain, versionSub, versionFlag + 1);
						} else {
							MyLog.i("PolicyService", "mDownloadinNo = "
									+ mDownloadinNo + ", versionFlag = "
									+ versionFlag);
						}
					}
				} catch (Exception e) {
					stopSelf();
				}

			}

		});
	}

	private void mergeFile(String fileName, int versionSize, String path) {
		ArrayList<File> tempFiles = new ArrayList<File>();
		for (int i = 0; i < versionSize; i++) {
			File tempFile = new File(Constance.PolicyFolderPath + path, "" + i);
			tempFiles.add(tempFile);
		}
		String realFilePath = Constance.PolicyFolderPath + fileName;
		File realFile = new File(realFilePath);
		FileUtils.mergeFiles(realFile, tempFiles);

	}

	private void PolicyConfirm(int versionMain, int versionSub) {
		try {
			MyLog.e("PolicyService", "versionMain " + versionMain
					+ " versionSub " + versionSub);
			CheckPolicyVersionMessage checkPolicyVersionMessage = new CheckPolicyVersionMessage(
					versionMain, versionSub);
			client.sendMessage(checkPolicyVersionMessage,
					new ActivityCallBackListener() {

						@Override
						public void doTimeOut() {
							MyLog.e("PolicyService", "PolicyConfirm doTimeOut");
							stopSelf();
						}

						@Override
						public void callBack(SoMessage message) {
							MyLog.i("PolicyService", "PolicyConfirm callBack");
							CheckPolicyVersionRespMessage checkPolicyVersionRespMessage = new CheckPolicyVersionRespMessage(
									message);
							int result = checkPolicyVersionRespMessage
									.getResult();
							System.out.println("callBack" + result);
							stopSelf();
						}
					}, 10);
		} catch (Exception e) {
			MyLog.e("PolicyService", "PolicyConfirm Exception");
			stopSelf();
		}

	}

	/**
	 * 跳转
	 */
	private void Skip(String path) {
		mFileNmaes.clear();
		mFileNmaes.add(path);
		Intent intent = new Intent();
		intent.setClass(MyApplicationLike.getContext(), ReadPDFActivity.class);
		MyLog.e("PolicyService", "isAppOnForeground:" + isAppOnForeground());
		if (isAppOnForeground()) {// 程序在前台
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle bundle = new Bundle();
			bundle.putSerializable("pathList", mFileNmaes);
			intent.putExtras(bundle);
			MyApplicationLike.getContext().startActivity(intent);
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

}
