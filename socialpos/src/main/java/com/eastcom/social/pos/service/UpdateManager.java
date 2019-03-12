package com.eastcom.social.pos.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.eastcom.social.pos.config.Constance;
import com.eastcom.social.pos.minterface.DownloadCallback;
import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.impl.FileVolumeManager;
import com.github.junrar.rarfile.FileHeader;

/**
 * apk，图片下载管理器
 * @author eronc
 *
 */
public class UpdateManager {

	private String TAG = "UpdateManager";
	private Context mContext;
	private ProgressDialog mProgressDialog;
	private static final String API_HOST = Constance.API_HOST;
	private String URL = "/s/api/store/app/";
	private String urlString = "";
	private String apkName = "demo.apk",logoName = "demo.jpg",photoName = "demo.rar";
	private String fileName ="";
	private String operateFlag ="";
	private static String dir = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/SocialStore/";
	// apk/logo/photo所在的文件路径，位于内置文件存储路径下。
    private static final String apkDir = dir+"apk/";
    private static final String logoDir = dir+"logo/";
    private static final String photoDir = dir+"photo/";
    public static boolean downloadPhotoSucces = false,downloadLogoSucces = false;
    private static String appName="";
    public static Map<Integer, String> photoMap = new HashMap<Integer, String>();
	private DownloadCallback mCallback;
	private SharedPreferences share;
	private SharedPreferences.Editor editor;
    
	public UpdateManager(Context context) {
		this.mContext = context;
		share = mContext.getSharedPreferences("User_SocialStore", 0);
		editor = share.edit();
	}
	
	/**
	 * 
	 * @param appId
	 * @param flag: apk,logo,photo
	 * @param name:作为.apk,logo(.jpg),photo(.rar---1.jpg,--2.jpg,--3.jpg)的文件名称
	 */
	public void checkUpdateInfo(String appId,String flag,String name) {
		appName = name;
		operateFlag = flag;
		urlString = API_HOST + URL + flag + "/" + appId;
		if(flag.equals("apk")){
			apkName = name + ".apk";
			fileName = apkName;
			dir = apkDir;
			sendOpenBroadcast();
			showDownloadProgress(urlString);
//			showDownloadTip(urlString);
		}else if(flag.equals("logo")){
			logoName = name + ".jpg";
			fileName = logoName;
			dir = logoDir;
			// 执行异步下载任务
			final DownloadTask downloadTask = new DownloadTask();
			downloadTask.execute(urlString);
		}else if(flag.equals("photo")){
			photoName = name + ".rar";
			fileName = photoName;
			dir = photoDir;
			// 执行异步下载任务
			final DownloadTask downloadTask = new DownloadTask();
			downloadTask.execute(urlString);
		}
	}

	/**
	 * 提示用户是否现在下载更新
	 * 
	 * @param id
	 */
	private void showDownloadTip(final String urlString) {
		new AlertDialog.Builder(mContext).setTitle("提示")
				.setMessage("是否现在下载更新?")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
					@TargetApi(11)
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						showDownloadProgress(urlString);
					}
				})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
					@TargetApi(11)
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).show();
	}

	/**
	 * 初始化下载进度条并开始下载任务
	 * 
	 * @param appId
	 */
	private void showDownloadProgress(String urlString) {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setMessage("正在下载...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setCancelable(true); // 设置可中途取消下载更新
		// 执行异步下载任务
		final DownloadTask downloadTask = new DownloadTask();
		downloadTask.execute(urlString);
		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				downloadTask.cancel(true);
				File file = new File(dir, fileName);
				if (file.exists()) { // 下载中途退出后应删除下载不完整的apk文件
					file.delete();
				}
			}
		});
	}

	/**
	 * 使用AsyncTask异步下载APK
	 * 
	 */
	private class DownloadTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(operateFlag.equals("apk")){
				mProgressDialog.show();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			InputStream input = null;
			OutputStream output = null;
			HttpURLConnection connection = null; 
			try {
				URL url = new URL(params[0]);
				Log.d(TAG, "请求的url是--"+url);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return "Server returned HTTP "
							+ connection.getResponseCode() + " "
							+ connection.getResponseMessage();
				}
				// 获取下载内容的长度
				// 如果返回 -1 表示服务器没有返回内容长度
				int fileLength = connection.getContentLength();
				input = connection.getInputStream();
				File file = new File(dir);
				if (!file.exists()) {
					file.mkdirs();
				}
				file = new File(dir, fileName);
				output = new FileOutputStream(file);
				byte data[] = new byte[4096];
				long total = 0;
				int count;
				while ((count = input.read(data)) != -1) {
					// 始终检测下载是否已经被取消，如果中途中断下载，将直接break，且不会执行onPostExecute()。
					if (isCancelled()) {
						break;
					}
					total += count;
					// 下载进度
					if (fileLength > 0) // 只有在下载文件长度知道的情况下
						publishProgress((int) (total * 100 / fileLength));
					output.write(data, 0, count);
				}
			} catch (Exception e) {
				return e.toString();
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}
				if (connection != null)
					connection.disconnect();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			if(operateFlag.equals("apk")){
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setMax(100);
				mProgressDialog.setProgress(progress[0]);
			}
		}

		@Override
		protected void onPostExecute(String result) {
			if(operateFlag.equals("apk")){
				mProgressDialog.dismiss();  
				
			}
			if (result != null) {
				Log.e(TAG, "文件系在出错--result--"+result);
				sendCloseBroadcast();
			} else {
				File file = new File(dir, fileName);
				if(operateFlag.equals("apk")){
					installApk(file); // 下载完成后直接执行安装程序
				}else if(operateFlag.equals("logo")){
					//处理下载的logo并返回true
					editor.putString(appName+"logo", Uri.fromFile(new File(dir, fileName))+"");
					editor.commit();
					mCallback.isDownloadLogoSucess(true);
				}else if(operateFlag.equals("photo")){
					//处理下载的logo并返回true
					unpackRar(dir, fileName);
					mCallback.isDownloadPhotoSucess(true);
				}
			}
		}
	}
	
	/**
	 * 解压.rar文件到path文件目录下
	 * 
	 * @param path
	 * @param rarName
	 */
	private static void unpackRar(String path, String rarName) {
		String filename = path + rarName;
		File f = new File(filename);
		Archive archive = null;
		try {
			archive = new Archive(new FileVolumeManager(f));
		} catch (RarException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (archive != null) {
			archive.getMainHeader().print();
			FileHeader fh = archive.nextFileHeader();
			int i = 0;
			while (fh != null) {
				try {
					File out = new File(path + appName + "_"+ fh.getFileNameString().trim());
					System.out.println(out.getAbsolutePath());
					FileOutputStream os = new FileOutputStream(out);
					archive.extractFile(fh, os);
					os.close();
					downloadPhotoSucces = true;
					photoMap.put(i,path + appName + "_"+ fh.getFileNameString().trim());
					i++;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					downloadPhotoSucces = false;
				} catch (RarException e) {
					e.printStackTrace();
					downloadPhotoSucces = false;
				} catch (IOException e) {
					e.printStackTrace();
					downloadPhotoSucces = false;
				}
				fh = archive.nextFileHeader();
			}
		}
	}
	

	public void setDownloadCallback(DownloadCallback mCallback) {
		this.mCallback = mCallback;
	}
	
	/**
	 * 安装APK
	 * 
	 * @param file
	 */
	private void installApk(File file) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			mContext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 卸载apk
	 * @param pkgName
	 */
	public void unInstallApk(String pkgName){
		try {
			Uri packageURI = Uri.parse("package:"+pkgName);   
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);   
			mContext.startActivity(uninstallIntent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 打开APP
	 * @param pkgName
	 */
	public void openApp(String pkgName) {
        try {
			Intent intent1 = mContext.getPackageManager().getLaunchIntentForPackage(pkgName);
			mContext.startActivity(intent1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送 打开未知来源选项广播
	 */
	private void sendOpenBroadcast() {
		Intent intent = new Intent();
		intent.setAction("android.intent.for.install.y");
		mContext.sendBroadcast(intent);
	}

	
	/**
	 * 发送 关闭未知来源选项广播
	 */
	private void sendCloseBroadcast() {
		Intent intent = new Intent();
		intent.setAction("android.intent.for.install.n");
		mContext.sendBroadcast(intent);
	}
	
}
