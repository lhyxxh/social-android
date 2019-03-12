package com.eastcom.social.pos.entity;

import java.util.Date;
import java.util.List;

public class UploadTradeEntity {

	private int tradeType;
	private int tradeState;
	private int preTradeNo;
	private String bankCardNo;
	private String sbCardNo;
	private String idCardNo;
	private String refNo;
	private Date tradeTime;
	private String tradeSn;
	private int tradeMoney;
	private String psamNo;
	private String rfsamNo;
	private int random;
	private String mac;
	private List<UploadTradeDetailEntity> detail;
	public int getTradeType() {
		return tradeType;
	}
	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}
	public int getTradeState() {
		return tradeState;
	}
	public void setTradeState(int tradeState) {
		this.tradeState = tradeState;
	}
	public int getPreTradeNo() {
		return preTradeNo;
	}
	public void setPreTradeNo(int preTradeNo) {
		this.preTradeNo = preTradeNo;
	}
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public String getSbCardNo() {
		return sbCardNo;
	}
	public void setSbCardNo(String sbCardNo) {
		this.sbCardNo = sbCardNo;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getRefNo() {
		return refNo;
	}
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	public Date getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}
	public String getTradeSn() {
		return tradeSn;
	}
	public void setTradeSn(String tradeSn) {
		this.tradeSn = tradeSn;
	}
	public int getTradeMoney() {
		return tradeMoney;
	}
	public void setTradeMoney(int tradeMoney) {
		this.tradeMoney = tradeMoney;
	}
	public String getPsamNo() {
		return psamNo;
	}
	public void setPsamNo(String psamNo) {
		this.psamNo = psamNo;
	}
	public String getRfsamNo() {
		return rfsamNo;
	}
	public void setRfsamNo(String rfsamNo) {
		this.rfsamNo = rfsamNo;
	}
	public int getRandom() {
		return random;
	}
	public void setRandom(int random) {
		this.random = random;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public List<UploadTradeDetailEntity> getDetail() {
		return detail;
	}
	public void setDetail(List<UploadTradeDetailEntity> detail) {
		this.detail = detail;
	}
	
	
	
}
