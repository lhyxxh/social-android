package com.eastcom.social.pos.core.orm.entity;


public class TradeDetail {
	private String id;
	private String fk_trade_id;
	private int detail_trade;
	private String bar_code;
	private String super_vision_code;
	private int actual_price;
	private int amount;
	private int social_category;
	private int is_upload;
	private String product_name;
	private int trade_type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFk_trade_id() {
		return fk_trade_id;
	}
	public void setFk_trade_id(String fk_trade_id) {
		this.fk_trade_id = fk_trade_id;
	}
	public int getDetail_trade() {
		return detail_trade;
	}
	public void setDetail_trade(int detail_trade) {
		this.detail_trade = detail_trade;
	}
	public String getBar_code() {
		return bar_code;
	}
	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}
	public String getSuper_vision_code() {
		return super_vision_code;
	}
	public void setSuper_vision_code(String super_vision_code) {
		this.super_vision_code = super_vision_code;
	}
	public int getActual_price() {
		return actual_price;
	}
	public void setActual_price(int actual_price) {
		this.actual_price = actual_price;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getSocial_category() {
		return social_category;
	}
	public void setSocial_category(int social_category) {
		this.social_category = social_category;
	}
	public int getIs_upload() {
		return is_upload;
	}
	public void setIs_upload(int is_upload) {
		this.is_upload = is_upload;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	
	
	public int getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(int trade_type) {
		this.trade_type = trade_type;
	}
	public TradeDetail() {
		super();
	}
	public TradeDetail(String id, String fk_trade_id, int detail_trade,
			String bar_code, String super_vision_code, int actual_price,
			int amount, int social_category, int is_upload, String product_name,int trade_type) {
		super();
		this.id = id;
		this.fk_trade_id = fk_trade_id;
		this.detail_trade = detail_trade;
		this.bar_code = bar_code;
		this.super_vision_code = super_vision_code;
		this.actual_price = actual_price;
		this.amount = amount;
		this.social_category = social_category;
		this.is_upload = is_upload;
		this.product_name = product_name;
		this.trade_type = trade_type;
	}

	
}
