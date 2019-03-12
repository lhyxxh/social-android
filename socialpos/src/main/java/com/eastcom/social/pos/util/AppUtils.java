package com.eastcom.social.pos.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Environment;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.service.LocalDataFactory;

public class AppUtils {

	public static final String ACCEPT_FORWORD = " iptables -A OUTPUT -d ";
	public static final String ACCEPT_BEHIND = " -j ACCEPT ";
	public static final String ALL_DROP = " iptables -A OUTPUT -j DROP ";
	public static final String CHECK = " iptables -nL OUTPUT ";
	public static final String DROP = " iptables -D OUTPUT ";

	public static final String REBOOT = "reboot";// 关机

	/**
	 * 方法描述：判断某一应用是否正在运行
	 * 
	 * @param context
	 *            上下文
	 * @param packageName
	 *            应用的包名
	 * @return true 表示正在运行，false表示没有运行
	 */
	public static boolean isAppRunning(Context context, String packageName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
		if (list.size() <= 0) {
			return false;
		}

		for (ActivityManager.RunningTaskInfo info : list) {
			if (info.baseActivity.getPackageName().equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 方法描述：判断某一Service是否正在运行
	 * 
	 * @param context
	 *            上下文
	 * @param serviceName
	 *            Service的全路径： 包名 + service的类名
	 * @return true 表示正在运行，false 表示没有运行
	 */
	public static boolean isServiceRunning(Context context, String serviceName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> runningServiceInfos = am
				.getRunningServices(200);
		if (runningServiceInfos.size() <= 0) {
			return false;
		}
		for (ActivityManager.RunningServiceInfo serviceInfo : runningServiceInfos) {
			if (serviceInfo.service.getClassName().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 发送adb命令
	 * 
	 */
	public static void execCmd(String cmd) {
		try {
			Process process = Runtime.getRuntime().exec("su");
			DataOutputStream out = new DataOutputStream(
					process.getOutputStream());
			out.writeBytes(cmd + " \n");
			out.writeBytes("exit\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加白名单地址
	 */
	public static void addWhiteList() {
		for (int i = 0; i < 20; i++) {
			AppUtils.execCmd(AppUtils.DROP + 1);
		}

		AppUtils.execCmd(AppUtils.ACCEPT_FORWORD + "120.25.230.37"
				+ AppUtils.ACCEPT_BEHIND);
		AppUtils.execCmd(AppUtils.ACCEPT_FORWORD + "120.24.72.174"
				+ AppUtils.ACCEPT_BEHIND);
		AppUtils.execCmd(AppUtils.ACCEPT_FORWORD + "112.91.147.170"
				+ AppUtils.ACCEPT_BEHIND);
		AppUtils.execCmd(AppUtils.ACCEPT_FORWORD + "120.24.242.201"
				+ AppUtils.ACCEPT_BEHIND);
		AppUtils.execCmd(AppUtils.ACCEPT_FORWORD + "131.252.88.188"
				+ AppUtils.ACCEPT_BEHIND);
		AppUtils.execCmd(AppUtils.ACCEPT_FORWORD + "120.25.210.39"
				+ AppUtils.ACCEPT_BEHIND);
		AppUtils.execCmd(AppUtils.ALL_DROP);

	}

	/**
	 * 拷贝 assets/so 中的so文件到 system/lib 下
	 * 
	 * @param context
	 */
	public static void soCopyLib(Context context, String soName) {
		AssetManager am = context.getAssets();
		String[] path = null;
		try {
			// 列出files目录下的文件
			path = am.list("so");
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < path.length; i++) {
			InputStream is = null;
			try {
				// 根据上文的 ‘files’+文件名，拼成一个路径，用AssetManager打开一个输入流，读写数据。
				is = am.open("so/" + path[i]);
				write2SDFromInput(Environment.getExternalStorageDirectory()
						.getPath() + "/bank/", soName, is);
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public static void write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[1024];
			int len = -1;
			while ((len = (input.read(buffer))) != -1) {
				output.write(buffer, 0, len);
			}
			output.flush();
			
			execCmd("mount -o remount,rw /system");
			execCmd("busybox cp -f " + path + fileName
	                + " /system/lib/");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// 删除so文件
			file.delete();
			//更新修改so文件标志
			LocalDataFactory localDataFactory = LocalDataFactory.newInstance(MyApplicationLike.getContext());
			localDataFactory.putBoolean(LocalDataFactory.SOCOPYLIB, true);
			AppUtils.execCmd(REBOOT);
		} catch (IOException e) {
			MyLog.e("AppUtils", "error:"+e.getMessage());
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建新文件
	 * 
	 * @param path
	 * @return
	 */
	public static File creatSDFile(String path) {
		File file = new File(path);
		try {
			file.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 获取系统供应商
	 * 
	 * @return
	 */
	public static String getSystemSupplierSoName() {
		String supplierVersion = "";
		if (Build.DISPLAY
				.contains("rk3288-eng 4.4.4 KTU84Q eng.ubuntu.20170114.105207 test-keys-PC228170114V1.1_PPTECH")) {
			supplierVersion = "";
		} else if (Build.DISPLAY.contains("rk3288-eng 4.4.4 KTU84Q eng.ubuntu")
				|| Build.DISPLAY.contains("PPTECH")
				|| (Build.DISPLAY.contains("CDPC") && !Build.DISPLAY
						.contains("DZ04"))) {
			supplierVersion = "libril-rk29-dataonly.so";
		} else if (Build.DISPLAY.contains("Uarm Technology")
				|| (Build.DISPLAY.contains("CDPC") && !Build.DISPLAY
						.contains("DE04"))) {
			supplierVersion = "libyuga-ril-clm920.so";
		}
		return supplierVersion;
	}
	
	/**
	 * 执行adb指令，返回执行结果
	 * @param 执行结果
	 */
	public static boolean execCmdResult(String command) {
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





}
