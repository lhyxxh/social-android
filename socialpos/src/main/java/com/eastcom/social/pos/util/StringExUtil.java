package com.eastcom.social.pos.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class StringExUtil {

	/**
	 * 判空处理
	 * @param value
	 * @return
	 */
	public static boolean isNullOrEmpty(String value) {
		return (null == value || value.length() == 0);
	}

	/**
	 * 数组连接
	 * @param list
	 * @param connectChar
	 * @return
	 */
	public static String listToString(List<String> list, String connectChar) {
		StringBuilder str = new StringBuilder();
		if (null == list) {
			return null;
		}
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (i == size - 1)// 当循环到最后一个的时候 就不添加逗号,
			{
				str.append(list.get(i));
			} else {
				str.append(list.get(i));
				str.append(connectChar);
			}
		}
		return str.toString();
	}

	/**
	 * MD5加密
	 * @param string
	 * @return
	 */
	public static String md5(String string) {

		byte[] hash;
		try {

			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}
		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
}
