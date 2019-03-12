package com.eastcom.social.pos.entity;

import java.util.Date;

public class MyCipher {
	private String bandCardNo;
	private String socialCardNo;
	private String refNo;
	private Date tradeTime;
	private String tradeSn;
	private int tradeMoney;
	private String psamNo;
	private String terminalCode;
	private String rfsamNo;

	public String getBandCardNo() {
		return bandCardNo;
	}

	public void setBandCardNo(String bandCardNo) {
		this.bandCardNo = bandCardNo;
	}

	public String getSocialCardNo() {
		return socialCardNo;
	}

	public void setSocialCardNo(String socialCardNo) {
		this.socialCardNo = socialCardNo;
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

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	public String getRfsamNo() {
		return rfsamNo;
	}

	public void setRfsamNo(String rfsamNo) {
		this.rfsamNo = rfsamNo;
	}

}
