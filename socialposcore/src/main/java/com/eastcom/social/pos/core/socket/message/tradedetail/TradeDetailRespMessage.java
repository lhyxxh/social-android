package com.eastcom.social.pos.core.socket.message.tradedetail;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class TradeDetailRespMessage extends SoMessage {

	private int saveSuccess;

	private int exist;

	private int tradeRandom;

	private int tradeDetailNo;

	public TradeDetailRespMessage(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		this.saveSuccess = (int) body[0];
		this.exist = (int) body[1];
		this.tradeRandom = ByteUtils.bytes2Int(body, 2, 4);
		this.tradeDetailNo = ByteUtils.bytes2Int(body, 6, 4);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public int getSaveSuccess() {
		return saveSuccess;
	}

	public void setSaveSuccess(int saveSuccess) {
		this.saveSuccess = saveSuccess;
	}

	public int getExist() {
		return exist;
	}

	public void setExist(int exist) {
		this.exist = exist;
	}

	public int getTradeRandom() {
		return tradeRandom;
	}

	public void setTradeRandom(int tradeRandom) {
		this.tradeRandom = tradeRandom;
	}

	public int getTradeDetailNo() {
		return tradeDetailNo;
	}

	public void setTradeDetailNo(int tradeDetailNo) {
		this.tradeDetailNo = tradeDetailNo;
	}

}
