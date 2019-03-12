package com.eastcom.social.pos.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.eastcom.social.pos.core.orm.entity.PolicyDocument;

public class PolicyEntity {
	

	public void setAppListEntity(JSONArray jsonArray,
			ArrayList<PolicyDocument> list) {

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = jsonArray.optJSONObject(i);
			PolicyDocument entity = new PolicyDocument();
			entity.setId(json.optString("id"));
			entity.setCreateDate(json.optString("createDate"));
			entity.setUpdateDate(json.optString("updateDate"));
			entity.setName(json.optString("name"));
			entity.setVersionCode(json.optString("versionCode"));
			entity.setFileNo(json.optString("fileNo"));
			entity.setPublishDate(json.optString("publishDate"));
			entity.setIsPublishing(json.optString("isPublishing"));
			entity.setPublishStatusLbl(json.optString("publishStatusLbl"));
			list.add(entity);
		}
	}

}
