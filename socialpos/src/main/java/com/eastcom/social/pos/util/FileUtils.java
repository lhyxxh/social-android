package com.eastcom.social.pos.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.util.Log;

public class FileUtils {
	public static final int BUFSIZE = 1024 * 8;

	public static void mergeFiles(String outFile, String[] files) {
		FileChannel outChannel = null;
		System.out.println("Merge " + Arrays.toString(files) + " into "
				+ outFile);
		try {
			outChannel = new FileOutputStream(outFile).getChannel();
			for (String f : files) {
				Charset charset = Charset.forName("utf-8");
				CharsetDecoder chdecoder = charset.newDecoder();
				CharsetEncoder chencoder = charset.newEncoder();
				@SuppressWarnings("resource")
				FileChannel fc = new FileInputStream(f).getChannel();
				ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
				CharBuffer charBuffer = chdecoder.decode(bb);
				ByteBuffer nbuBuffer = chencoder.encode(charBuffer);
				while (fc.read(nbuBuffer) != -1) {

					bb.flip();
					nbuBuffer.flip();
					outChannel.write(nbuBuffer);
					bb.clear();
					nbuBuffer.clear();
				}
				fc.close();
			}
			System.out.println("Merged!! ");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (outChannel != null) {
					outChannel.close();
				}
			} catch (IOException ignore) {
			}
		}
	}

	/**
	 * 通过FileChannel方式 合并文件
	 * */
	public static void mergeFiles(File realFile, ArrayList<File> tempFiles) {
		FileChannel mFileChannel;
		try {
			FileOutputStream fos = new FileOutputStream(realFile);
			mFileChannel = fos.getChannel();
			FileChannel inFileChannel;
			for (File file : tempFiles) {
				inFileChannel = new FileInputStream(file).getChannel();
				// 下面应该根据不同文件减去相应的文件头（这里没有剪去文件头，实际应用中应当减去）
				inFileChannel.transferTo(0, inFileChannel.size(), mFileChannel);
				inFileChannel.close();
			}
			fos.close();
			mFileChannel.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 复制Assets下文件
	public static void CopyAssets(String fileName, String path, Context context) {
		try {
			InputStream in = null;
			OutputStream out = null;
			File outFile = new File(path + fileName);
			in = context.getAssets().open(fileName);
			out = new FileOutputStream(outFile);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getNewBankSys(String userno, String terminal,
			String usercname, String simplename) {
		StringBuffer sb = new StringBuffer();
		sb.append("[CONFIG]" + "\n");
		sb.append("USERNO=" + userno + "\n");
		sb.append("TERMINAL=" + terminal + "\n");
		sb.append("USERCNAME=" + usercname + "\n");
		sb.append("SIMPLENAME=" + simplename + "\n");
		sb.append("USERENAME=HUIFU" + "\n");
		sb.append("RUNMODE=0" + "\n");
		sb.append("TRANSSUCCPRINT=2" + "\n");
		sb.append("EXTERAPP=0" + "\n");
		sb.append("PINPADMES=0" + "\n");
		sb.append("IFSERVER=0" + "\n");
		sb.append("TRANSMESX=10" + "\n");
		sb.append("TRANSMESY=369" + "\n");
		sb.append("PACK_TYPE=1" + "\n");
		sb.append("TRANSEENCRYPT=0" + "\n");
		sb.append("READTRACKCARD=0" + "\n");
		sb.append("SUBFORMY=40" + "\n");
		sb.append("HOSTIP=131.252.88.188" + "\n");
		sb.append("STANDARDPORT=9996" + "\n");
		sb.append("DEVICESERIES=/dev/ttyS4" + "\n");
		sb.append("SERIALPORT=5" + "\n");
		sb.append("LTPPORT=5" + "\n");
		sb.append("IFSENDBACK=1" + "\n");
		sb.append("PREAUTHORIZED=0" + "\n");
		sb.append("AMOUNTEDIT=0" + "\n");
		sb.append("SIGNOUTSTATE=0" + "\n");
		sb.append("INPUTCASHER=0" + "\n");
		sb.append("BATCHNO=000002" + "\n");
		sb.append("PRODUCTNO=0000" + "\n");
		sb.append("SIGNSTATE=1" + "\n");
		sb.append("AUTOSIGNIN=1" + "\n");
		sb.append("AUTOINDATE=20170227" + "\n");
		sb.append("AUTOBALANCE=1" + "\n");
		sb.append("CONSUME=0" + "\n");
		sb.append("QUERYAMOUNT=0" + "\n");
		sb.append("SIGNIN=0" + "\n");
		sb.append("SIGNOUT=0" + "\n");
		sb.append("REPRINT=0" + "\n");
		sb.append("BALANCE=0" + "\n");
		sb.append("QUERYGLIDE=0" + "\n");
		sb.append("DAYTOTAL=0" + "\n");
		sb.append("PRINTSINGLE=0" + "\n");
		sb.append("REPEAL=0" + "\n");
		sb.append("REFUND=0" + "\n");
		sb.append("PRINTMODE=0" + "\n");
		sb.append("LEFTLENGTH=80" + "\n");
		sb.append("MIDLENGTH=5" + "\n");
		sb.append("STOPTIME=5" + "\n");
		sb.append("OFFSETROWS=0" + "\n");
		sb.append("CARDSCREEN=1" + "\n");
		sb.append("CARDSEND=0" + "\n");
		sb.append("BEGINPOSTION=6" + "\n");
		sb.append("ENDPOSTION=4" + "\n");
		sb.append("SOFTINFO=0" + "\n");
		sb.append("DEVICESN=0" + "\n");
		sb.append("OPERNO=0" + "\n");
		sb.append("CASHDEVICENO_MES=0" + "\n");
		sb.append("GLIDE=0" + "\n");
		sb.append("OTHERINFO=0" + "\n");
		sb.append("SHOPNO=0" + "\n");
		sb.append("TRANSDATE=0" + "\n");
		sb.append("TRANSTIME=0" + "\n");
		sb.append("CASHDEVICENO=1001" + "\n");
		sb.append("CASHER=01" + "\n");
		sb.append("SHOPID=" + "\n");
		sb.append("TRANSDATEVALUE=20090211" + "\n");
		sb.append("TRANSTIMEVALUE=145025" + "\n");
		sb.append("GLIDEVALUE=123456789012" + "\n");
		sb.append("SOFTINFOVALUE=02-01-10" + "\n");
		sb.append("DEVICESNVALUE=07CL3U2T000000000000" + "\n");
		sb.append("OTHERINFOVALUE=0" + "\n");
		sb.append("STRATUI=1" + "\n");
		sb.append("STARTCARDMISUIKEY=120" + "\n");
		sb.append("CARDMISREMARK=F8" + "\n");
		sb.append("RUNAPPNAME=" + "\n");
		sb.append("STRATCONFIG=1" + "\n");
		sb.append("STARTCONFIGKEY=119" + "\n");
		sb.append("CONFIGREMARK=" + "\n");
		sb.append("AUTORUN=0" + "\n");
		sb.append("LAST_PACK_TYPE=1" + "\n");
		sb.append("SERIALRECVTIMEOUT=60" + "\n");
		sb.append("SOCKETRECVTIMEOUT=60" + "\n");
		sb.append("TEST_SN=14F2B6D0944C3E76D4360EE764D0288A" + "\n");
		sb.append("SN=B40AF7F19096774EE43AD77035C88B72" + "\n");
		sb.append("3GEnable=0" + "\n");
		sb.append("SSLEnable=1" + "\n");
		sb.append("SSLIP=120.25.210.39" + "\n");
		sb.append("SSLPort=18001" + "\n");
		sb.append("\n\n\n\n\n\n");
		return sb.toString();
	}

	public static String writeBankSysUrl(String old, String tg,
			String sslEnable, String sslip, String sslport) {
		StringBuffer sb = new StringBuffer();
		sb.append(old);
		sb.append("3GEnable=" + tg + "\n");
		sb.append("SSLEnable=" + sslEnable + "\n");
		sb.append("SSLIP=" + sslip + "\n");
		sb.append("SSLPORT=" + sslport + "\n");
		sb.append("\n\n\n\n\n\n");
		return sb.toString();
	}

	// 写数据到SD中的文件
	public static void writeFileSdcardFile(String fileName, String write_str) {
		try {

			FileOutputStream fout = new FileOutputStream(fileName);

			OutputStreamWriter osw = new OutputStreamWriter(fout, "GBK");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(write_str);
			bw.close();
			osw.close();
			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 写二进制数据到SD中的文件
	/**
	 * 根据byte数组生成文件
	 * 
	 * @param byte[] 生成文件用到的byte数组
	 */
	public static void createFileWithByte(File file, byte[] bytes) {
		FileOutputStream outputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			outputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(outputStream);
			bufferedOutputStream.write(bytes);
			bufferedOutputStream.flush();
		} catch (Exception e) {
			MyLog.e("FileUtils",
					"createFileWithByte exception:" + e.getMessage());
		} finally {
			// 关闭创建的流对象
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bufferedOutputStream != null) {
				try {
					bufferedOutputStream.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	// 读SD中的文件
	public static String readFileSdcardFile(String fileName) {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);

			int length = fin.available();

			byte[] buffer = new byte[length];
			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "GBK");
			fin.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 　　* 　　* @param myContext 　　* @param ASSETS_NAME 要复制的文件名 　　* @param
	 * savePath 要保存的路径 　　* @param saveName 复制后的文件名 　　* testCopy(Context
	 * context)是一个测试例子。 　　
	 */
	public static void copyAssetsFile(Context myContext, String ASSETS_NAME,
			String savePath, String saveName) {
		String filename = savePath + "/" + saveName;
		File dir = new File(savePath);
		// 如果目录不中存在，创建这个目录
		if (!dir.exists())
			dir.mkdir();
		try {
			if (!(new File(filename)).exists()) {
				InputStream is = myContext.getResources().getAssets()
						.open(ASSETS_NAME);
				FileOutputStream fos = new FileOutputStream(filename);
				byte[] buffer = new byte[7168];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹以及目录下的文件
	 * 
	 * @param filePath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String filePath) {
		boolean flag = false;
		// 如果filePath不以文件分隔符结尾，自动添加文件分隔符
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		File dirFile = new File(filePath);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		File[] files = dirFile.listFiles();
		// 遍历删除文件夹下的所有文件(包括子目录)
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				// 删除子文件
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} else {
				// 删除子目录
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前空目录
		return dirFile.delete();
	}

	/**
	 * 删除单个文件
	 * 
	 * @param filePath
	 *            被删除文件的文件名
	 * @return 文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;
	}

	public static void writeToFile(String contents, File srcFile)
			throws Exception {
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			srcFile.createNewFile();
			fileOutputStream = new FileOutputStream(srcFile);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(contents.getBytes());
			bufferedOutputStream.flush();
		} catch (Exception e) {
			Log.e("zipFile", "Error on write File:" + e);
		} finally {
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
			if (bufferedOutputStream != null) {
				bufferedOutputStream.close();
			}
		}
	}

	/**
	 * 
	 * @param srcFile
	 * @param desFile
	 */
	public static void zipFile(File desFile, File srcFile) {
		try {

			GZIPOutputStream gzipOutputStream = null;
			FileInputStream fileInputStream = null;
			try {
				gzipOutputStream = new GZIPOutputStream(new FileOutputStream(
						desFile));
				fileInputStream = new FileInputStream(srcFile);
				byte[] buffer = new byte[1024];
				int len = -1;
				while ((len = (fileInputStream.read(buffer))) != -1) {
					gzipOutputStream.write(buffer, 0, len);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (gzipOutputStream != null) {
						gzipOutputStream.close();
					}
					if (gzipOutputStream != null) {
						fileInputStream.close();
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
