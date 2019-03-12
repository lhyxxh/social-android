package com.eastcom.social.pos.util;

import com.google.gson.Gson;

/**
 * 实现json字符串和实体类互转
 */
public class JsonUtils {

	public static String toJSONString(Object obj) {
		Gson gson = new Gson();
		String jsonString = gson.toJson(obj);
		return jsonString;
	}

}
