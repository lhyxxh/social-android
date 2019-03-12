package com.eastcom.social.pos.core.utils;

import com.google.gson.Gson;

public class JsonUtils {

    public static String toJSONString(Object obj) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(obj);
        return jsonString;
    }
}
