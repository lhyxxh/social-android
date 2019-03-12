package com.eastcom.social.pos.core.utils;

import android.annotation.SuppressLint;

/**
 * Byte 处理工具箱
 * @author wood
 *
 */
public class ByteUtils {
	public static String toHexString(byte... data) {
		StringBuilder sb = new StringBuilder();
		if (data != null && data.length > 0) {
			for (int i = 0; i < data.length; i++) {
				if (i == 0) {
					sb.append("0x");
				}

				String hv = Integer.toHexString(data[i] & 0xFF);
				sb.append(hv.length() < 2 ? "0" : "").append(hv);
			}
		}

		return sb.toString();
	}

	public static String toHexByteString(byte... data) {
		StringBuilder sb = new StringBuilder();
		if (data != null && data.length > 0) {
			for (int i = 0; i < data.length; i++) {
				if (i > 0) {
					if (i % 12 == 0) {
						sb.append("\r\n");
					} else {
						sb.append(" ");
					}
				}

				String hv = Integer.toHexString(data[i] & 0xFF);
				sb.append(hv.length() < 2 ? "0x0" : "0x").append(hv);
			}
		}

		return sb.toString();
	}

	public static byte[] intToBytes2(int data) {
		byte[] result = new byte[2];
		result[0] = (byte) ((0xff00 & data) >> 8);
		result[1] = (byte) (0xff & data);
		return result;
	}

	public static byte[] intToBytes3(int data) {
		byte[] result = new byte[3];
		result[0] = (byte) ((0xff0000 & data) >> 16);
		result[1] = (byte) ((0xff00 & data) >> 8);
		result[2] = (byte) (0xff & data);
		return result;
	}

	public static byte[] intToBytes(int data) {
		byte[] result = new byte[4];
		result[0] = (byte) ((0xff000000 & data) >> 24);
		result[1] = (byte) ((0xff0000 & data) >> 16);
		result[2] = (byte) ((0xff00 & data) >> 8);
		result[3] = (byte) (0xff & data);
		return result;
	}

	public static byte[] shortToBytes(short data) {
		byte[] result = new byte[2];
		result[0] = (byte) ((0xff00 & data) >> 8);
		result[1] = (byte) (0xff & data);
		return result;
	}

	public static byte[] byteToBytes(byte data) {
		byte[] result = new byte[1];
		result[0] = data;
		return result;
	}

	public static byte getChecksum(byte[]... data) {
		byte code = 0x00;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				code += data[i][j];
			}
		}
		code = (byte) (0x00 - code);

		return code;
	}

	/**
	 * 将byte转换为无符号的整形
	 */
	public static int byte2Int(byte b) {
		return bytes2Int(new byte[] { b });
	}

	/**
	 * 将bytes全部转换为无符号的整形
	 */
	public static int bytes2Int(byte[] b) {
		return bytes2Int(b, 0, b.length);
	}

	/**
	 * 将bytes转为无符号的整型
	 */
	public static int bytes2Int(byte[] from, int offset, int len) {
		int to = 0;
		int min = offset;
		to = 0;
		for (int i_move = len - 1, i_from = min; i_move >= 0; i_move--, i_from++) {
			to = to << 8 | (from[i_from] & 0xff);
		}
		return to;
	}

	/**
	 * 将16进制表示的字符串转为字节数组
	 */
	@SuppressLint("DefaultLocale")
	public static byte[] hexStr2bytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * 将单个字符转为字节
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 将byte转成16进制表示的字符串，不足位补零
	 */
	public static String byte2HexStr(byte b) {
		String str = Integer.toString(b & 0xff, 16);
		if (str.length() == 1) {
			str = "0" + str;
		}
		return str;
	}

	/**
	 * 将bytes全部转为16进制表示的字符串
	 */
	public static String bcd2Str(byte[] bytes) {
		return bcd2Str(bytes, 0, bytes.length);
	}

	/**
	 * 将bytes转为16进制表示的字符串
	 */
	public static String bcd2Str(byte[] bytes, int startIndex, int len) {
		StringBuffer temp = new StringBuffer();
		for (int i = startIndex; i < len + startIndex; i++) {
			String c = byte2HexStr(bytes[i]);
			temp.append(c);
		}
		return temp.toString();
	}

	/**
	 * 为字符串进行左/右补零
	 */
	public static String addZeroStr(String src, int len, boolean left) {
		String result = src;
		if (result == null) {
			result = "";
		}
		int srcLen = result.length();
		if (srcLen < len) {
			if (left) {
				for (int i = 0; i < len - srcLen; i++) {
					result = "0" + result;
				}
			} else {
				for (int i = 0; i < len - srcLen; i++) {
					result = result + "0";
				}
			}
		}

		return result;
	}

}
