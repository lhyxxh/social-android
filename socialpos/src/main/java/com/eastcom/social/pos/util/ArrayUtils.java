package com.eastcom.social.pos.util;


public class ArrayUtils {
	public static boolean contains(String[] arr, String targetValue) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].equals(targetValue)) {
				return true;
			}
		}
		return false;
	}
}
