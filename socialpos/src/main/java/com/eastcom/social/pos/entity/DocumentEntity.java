package com.eastcom.social.pos.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eastcom.social.pos.core.orm.entity.Document;


public class DocumentEntity {
	
	public void getList(JSONArray array, ArrayList<Document> result) {
		for (int i = 0; i < array.length(); i++) {
			JSONObject json = array.optJSONObject(i);
			Document pi = new Document();
			pi.setId(json.optString("id"));
			pi.setFileName(json.optString("fileName"));
			pi.setFkFileId(json.optString("fkFileId"));
			pi.setFileNo(json.optInt("fileNo"));
			pi.setFkSignboardId(json.optString("fkSignboardId"));
			pi.setIsDownload(json.optInt("isDownload"));
			pi.setIdRead(json.optInt("idRead"));
			pi.setCreateDate(json.optString("createDate"));
			pi.setDownload(json.optString("download"));
			pi.setReadDate(json.optString("readDate"));
			result.add(pi);
		}
	}


}
