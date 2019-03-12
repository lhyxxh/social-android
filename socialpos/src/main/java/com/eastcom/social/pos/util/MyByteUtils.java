package com.eastcom.social.pos.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;

import android.os.Environment;
import android.util.Log;

import com.eastcom.social.pos.activity.SettingActivity;
import com.eastcom.social.pos.config.Constance;

public class MyByteUtils {
	private final static String pText1 = "/sdcard/bank/P_Tacksingle.txt";
	private final static String pText2 = "/sdcard/social/P_Tacksingle.txt";

	/**
	 * 从一个byte[]数组中截取一部分
	 * 
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++)
			bs[i - begin] = src[i];
		return bs;
	}

	/**
	 * 从一个byte[]数组中截取一部分
	 * 
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static String subBytesToString(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++)
			bs[i - begin] = src[i];

		String srt = "";
		try {
			srt = new String(bs, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return srt;
	}

	/**
	 * 根据byte数组长度截取字符串
	 * 
	 * @param byteLength
	 * @param src
	 * @return
	 */
	public static String getByte2String(int byteStart, int byteEnd, String src) {
		if (!Constance.liandi) {
			return src.substring(byteStart, byteEnd);
		} else {
			int n1 = 0;
			int i1 = 0;
			int n2 = 0;
			int i2 = 0;
			char[] c = src.toCharArray();
			while (n1 < byteEnd) {

				if ((c[i1] >= 0x4e00) && (c[i1] <= 0x9fa5)) {
					n1 = n1 + 2;
				} else {
					n1++;
				}
				i1++;
			}
			while (n2 < byteStart) {

				if ((c[i2] >= 0x4e00) && (c[i2] <= 0x9fa5)) {
					n2 = n2 + 2;
				} else {
					n2++;
				}
				i2++;
			}
			return src.substring(i2, i1);
		}

	}

	/**
	 * 根据byte数组长度截取字符串
	 * 
	 * @param byteLength
	 * @param src
	 * @return
	 */
	public static String getByteString2String(int byteStart, int byteEnd,
			String src) {
		return src.substring(byteStart, byteEnd);
	}

	/**
	 * 
	 * 删除指定目录下文件及目录
	 * 
	 * 
	 * 
	 * @param deleteThisPath
	 * 
	 * @param filepath
	 * 
	 * @return
	 */

	public static void deleteFolderFile(String filePath, boolean deleteThisPath)

	throws IOException {

		if (!"".equals(filePath)) {

			File file = new File(filePath);

			if (file.isDirectory()) {// 处理目录

				File files[] = file.listFiles();

				for (int i = 0; i < files.length; i++) {

					deleteFolderFile(files[i].getAbsolutePath(), true);

				}

			}

			if (deleteThisPath) {

				if (!file.isDirectory()) {// 如果是文件，删除

					file.delete();

				} else {// 目录

					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除

						file.delete();

					}

				}

			}

		}

	}

	// 读取文本文件中的内容
	public static String ReadTxtFile(String strFilePath) {
		String path = strFilePath;
		String content = ""; // 文件内容字符串
		// 打开文件
		File file = new File(path);
		// 如果path是传递过来的参数，可以做一个非目录的判断
		if (file.isDirectory()) {
			Log.d("TestFile", "The File doesn't not exist.");
		} else {
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null) {
					InputStreamReader inputreader = new InputStreamReader(
							instream, "GBK");
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					// 分行读取
					while ((line = buffreader.readLine()) != null) {
						content += line + "\n";
					}
					instream.close();
				}
			} catch (java.io.FileNotFoundException e) {
				Log.d("TestFile", "The File doesn't not exist.");
			} catch (IOException e) {
				Log.d("TestFile", e.getMessage());
			}
		}
		return content;
	}

	// 读取打印文件
	public static String ReadTxtFile() {
		String Text = Constance.liandi ? pText1 : pText2;
		return ReadTxtFile(Text);
	}

	// 删除打印文件
	public static void deleteFolderFile() {
		String Text = Constance.liandi ? pText1 : pText2;
		try {
			getSysCfg(true);
			deleteFolderFile(Text, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *  获取pos机配置文件
	 * @param flag 是否根据备份文件是否存在再备份
	 */
	public static void getSysCfg(boolean flag) {
		try {
			MyLog.i("getSysCfg", "getSysCfg");
			File exportDir = new File(
					Environment.getExternalStorageDirectory(),
					SettingActivity.BACKUP);
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}
			File backup = new File(exportDir, "SysCfg.dat");

			String fileName = "/mnt/sdcard/bank/SysCfg.dat";
			File copyFile = new File(fileName);
			if (copyFile.exists()) {
				if (flag) {
					if (backup.exists()) {
					} else {
						MyLog.i("getSysCfg", "back up");
						backup.createNewFile();
						fileCopy(copyFile, backup);
					}
				}else {
					MyLog.i("getSysCfg", "back up");
					backup.createNewFile();
					fileCopy(copyFile, backup);
				}
				
				
			} else {
				MyLog.i("getSysCfg", "copyFile not exist");
				if (backup.exists()) {
					copyFile.createNewFile();
					fileCopy(backup, copyFile);
				} else {
					MyLog.i("getSysCfg", "copyFile & backup not exist");
				}
			}

		} catch (Exception e) {
			MyLog.e("getSysCfg", "Exception");
		}

	}

	private static void fileCopy(File copyFile, File backup) throws IOException {
		FileChannel inChannel = new FileInputStream(copyFile).getChannel();
		FileChannel outChannel = new FileOutputStream(backup).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}

}
