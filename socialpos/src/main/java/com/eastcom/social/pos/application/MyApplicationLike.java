package com.eastcom.social.pos.application;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.eastcom.social.pos.service.AuthService;
import com.eastcom.social.pos.service.FeedDogService;
import com.eastcom.social.pos.service.GPRSService;
import com.eastcom.social.pos.service.LocationService;
import com.eastcom.social.pos.service.PolicyService;
import com.eastcom.social.pos.service.QueryMedicineService;
import com.eastcom.social.pos.service.SlowUploadService;
import com.eastcom.social.pos.service.TimerService;
import com.eastcom.social.pos.service.UpdateService;
import com.eastcom.social.pos.service.UpdateSoftService;
import com.eastcom.social.pos.service.UploadIncreamentVersionService;
import com.eastcom.social.pos.service.UploadService;
import com.eastcom.social.pos.service.UploadTradeFileService;
import com.eastcom.social.pos.service.UploadVersionService;
import com.eastcom.social.pos.util.AppUtils;
import com.eastcom.social.pos.util.MyLog;
import com.qihoo.linker.logcollector.LogCollector;
import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.lib.listener.DefaultPatchListener;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.reporter.DefaultLoadReporter;
import com.tencent.tinker.lib.reporter.DefaultPatchReporter;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import java.io.DataOutputStream;

/**
 * Created by ljj on 2016/6/14.
 */
@DefaultLifeCycle(application = "com.eastcom.social.pos.application.MyApplication",
		flags = ShareConstants.TINKER_ENABLE_ALL,
		loadVerifyFlag = false)
public class MyApplicationLike extends DefaultApplicationLike {
	private static Context mContext;
	public static LocationService locationService;
	public Vibrator mVibrator;
	
	public static Intent updateSoftService ;//更新固件服务
	public static Intent updateIncrementService; //更新增量包服务
	public static Intent uploadService;
	public static Intent updateService;//http更新固件服务
	public static Intent slowUplodeService;
	public static Intent gprsService;
	public static Intent timerService;
	public static Intent policyService;
	public static Intent queryMedicineService;
	public static Intent uploadVersionService;
	public static Intent uploadTradeFileService;
	
	public static Intent authService;
	public static Intent feedDogService;

	public MyApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
		super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
	}

	@Override
	public void onBaseContextAttached(Context base) {
		super.onBaseContextAttached(base);
		MultiDex.install(base);
//        TinkerInstaller.install(this);
//        TinkerInstaller.install(this, new DefaultLoadReporter(getApplication())
//                , new DefaultPatchReporter(getApplication()), new DefaultPatchListener(getApplication()), MyTinkerResultService.class, new UpgradePatch());
		TinkerInstaller.install(this, new MyLoadReporter(getApplication())
				, new DefaultPatchReporter(getApplication()), new DefaultPatchListener(getApplication()), MyTinkerResultService.class, new UpgradePatch());
		Tinker tinker = Tinker.with(getApplication());
	}

	@Override
	public void onCreate() {
		super.onCreate();
//		execCmd("chmod -R 777 /dev/bus/usb");
		mContext = getApplication();
		MyLog.i("MyApplicationLike", "onCreate1");
		initDebug();
		initBaiduMap();
		initService();
	}
	/**
	 * 执行命令
	 *
	 * @param command 1、获取root权限 "chmod 777 "+getPackageCodePath() 2、关机 reboot
	 *                -p 3、重启 reboot
	 */
	public static boolean execCmd(String command) {
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (process != null) {
					process.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	/**
	 * 初始化服务
	 */
	private void initService() {
		updateSoftService = new Intent(MyApplicationLike.getContext(), UpdateSoftService.class);
		updateIncrementService = new Intent(MyApplicationLike.getContext(), UploadIncreamentVersionService.class);
		uploadService = new Intent(MyApplicationLike.getContext(), UploadService.class);
		updateService = new Intent(MyApplicationLike.getContext(), UpdateService.class);
		slowUplodeService = new Intent(MyApplicationLike.getContext(), SlowUploadService.class);
		gprsService = new Intent(MyApplicationLike.getContext(), GPRSService.class);
		timerService = new Intent(MyApplicationLike.getContext(), TimerService.class);
		policyService = new Intent(MyApplicationLike.getContext(), PolicyService.class);
		queryMedicineService = new Intent(MyApplicationLike.getContext(), QueryMedicineService.class);
		authService = new Intent(MyApplicationLike.getContext(), AuthService.class);
		feedDogService = new Intent(MyApplicationLike.getContext(), FeedDogService.class);
		uploadVersionService = new Intent(MyApplicationLike.getContext(), UploadVersionService.class);
		uploadTradeFileService = new Intent(MyApplicationLike.getContext(), UploadTradeFileService.class);
	}

	private void initDebug() {
		boolean isDebug = true;
		LogCollector.setDebugMode(isDebug);
		LogCollector.init(mContext, null, null);// params can be null
	}

	private void initBaiduMap() {
		/***
		 * 初始化定位sdk，建议在Application中创建
		 */
		locationService = new LocationService(getApplication());
		mVibrator = (Vibrator) getApplication().getSystemService(
				Service.VIBRATOR_SERVICE);
		SDKInitializer.initialize(getApplication());
	}


	public static Context getContext() {
		return mContext;
	}
	
	/**
	 * 关闭耗时服务
	 * @param flag 是否只关闭更新固件服务
	 */
	public static void stopTimeConsumingService(boolean flag) {
		boolean policyServiceRunning = AppUtils.isServiceRunning(MyApplicationLike.getContext(), "com.eastcom.social.pos.service.PolicyService");
		if (!flag && policyServiceRunning) {
			mContext.stopService(policyService);
		}
		boolean updateSoftServiceRunning = AppUtils.isServiceRunning(MyApplicationLike.getContext(), "com.eastcom.social.pos.service.UpdateSoftService");
		if (updateSoftServiceRunning) {
			mContext.stopService(updateSoftService);
		}
	}
	
}
