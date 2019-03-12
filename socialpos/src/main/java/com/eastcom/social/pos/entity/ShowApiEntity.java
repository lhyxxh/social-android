package com.eastcom.social.pos.entity;

import org.json.JSONObject;

public class ShowApiEntity {
	private String spec;//剂型
	private String flag;//是否成功标志
	private String code;//条码
	private String goodsName;//商品名
	private String manuName;//厂家
	private String img;//照片地址
	private String ret_code;//返回代码
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getManuName() {
		return manuName;
	}
	public void setManuName(String manuName) {
		this.manuName = manuName;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getRet_code() {
		return ret_code;
	}
	public void setRet_code(String ret_code) {
		this.ret_code = ret_code;
	}
	public void getItem(JSONObject json, ShowApiEntity result) {

		result.setCode(json.optString("code"));
		result.setFlag(json.optString("flag"));
		result.setGoodsName(json.optString("goodsName"));
		result.setImg(json.optString("img"));
		result.setManuName(json.optString("manuName"));
		result.setRet_code(json.optString("ret_code"));
		result.setSpec(json.optString("spec"));

	}
}
