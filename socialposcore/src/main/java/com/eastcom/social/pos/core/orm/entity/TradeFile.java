package com.eastcom.social.pos.core.orm.entity;

import java.util.Date;

/**
 * 交易文件
 */
public class TradeFile {
	private String tradeFileName;// 交易文件名
	private int tradeType;// 交易类型
	private Date tradeDate;// 交易时间
	private int uploadStatus;// 上传状态

	public TradeFile(String tradeFileName, int tradeType, Date tradeDate,
			int uploadStatus) {
		this.tradeFileName = tradeFileName;
		this.tradeType = tradeType;
		this.tradeDate = tradeDate;
		this.uploadStatus = uploadStatus;
	}

	public TradeFile() {
	}

	public String getTradeFileName() {
		return this.tradeFileName;
	}

	public void setTradeFileName(String tradeFileName) {
		this.tradeFileName = tradeFileName;
	}

	public int getTradeType() {
		return this.tradeType;
	}

	public void setTradeType(int tradeType) {
		this.tradeType = tradeType;
	}

	public Date getTradeDate() {
		return this.tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public int getUploadStatus() {
		return this.uploadStatus;
	}

	public void setUploadStatus(int uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
}
