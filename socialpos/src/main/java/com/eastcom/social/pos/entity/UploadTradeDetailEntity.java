package com.eastcom.social.pos.entity;


public class UploadTradeDetailEntity {

	private String barCode;
	private String superVisionCode;
	private int actualPrice;
	private int amount;
	private int socialCategory;
	private int tradeDetailNo;
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getSuperVisionCode() {
		return superVisionCode;
	}
	public void setSuperVisionCode(String superVisionCode) {
		this.superVisionCode = superVisionCode;
	}
	public int getActualPrice() {
		return actualPrice;
	}
	public void setActualPrice(int actualPrice) {
		this.actualPrice = actualPrice;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getSocialCategory() {
		return socialCategory;
	}
	public void setSocialCategory(int socialCategory) {
		this.socialCategory = socialCategory;
	}
	public int getTradeDetailNo() {
		return tradeDetailNo;
	}
	public void setTradeDetailNo(int tradeDetailNo) {
		this.tradeDetailNo = tradeDetailNo;
	}
	
	
	
}
