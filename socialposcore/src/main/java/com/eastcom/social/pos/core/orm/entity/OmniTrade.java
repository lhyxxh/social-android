package com.eastcom.social.pos.core.orm.entity;

/**
 * 全渠道交易
 */
public class OmniTrade {
	
	private String outTradeNo;//
	private String busCode;//
	private String payChannel;//
	private String merchantNo;//
	private String cannelOutTradeNo;//
	private String refundOutTradeNo;//
	private String transactionId;//
	private String totalFee;//
	private String date;//
	private String payTime;//

	public OmniTrade() {}
	
	public OmniTrade(String outTradeNo, String busCode, String payChannel,
			String merchantNo, String cannelOutTradeNo,
			String refundOutTradeNo, String transactionId, String totalFee,
			String date, String payTime) {
		super();
		this.outTradeNo = outTradeNo;
		this.busCode = busCode;
		this.payChannel = payChannel;
		this.merchantNo = merchantNo;
		this.cannelOutTradeNo = cannelOutTradeNo;
		this.refundOutTradeNo = refundOutTradeNo;
		this.transactionId = transactionId;
		this.totalFee = totalFee;
		this.date = date;
		this.payTime = payTime;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getBusCode() {
		return busCode;
	}

	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getCannelOutTradeNo() {
		return cannelOutTradeNo;
	}

	public void setCannelOutTradeNo(String cannelOutTradeNo) {
		this.cannelOutTradeNo = cannelOutTradeNo;
	}

	public String getRefundOutTradeNo() {
		return refundOutTradeNo;
	}

	public void setRefundOutTradeNo(String refundOutTradeNo) {
		this.refundOutTradeNo = refundOutTradeNo;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}


}
