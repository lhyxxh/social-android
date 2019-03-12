package com.eastcom.social.pos.core.socket.message.tradedetail;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

import android.annotation.SuppressLint;

public class TradeDetailMessage extends SoMessage {

	private String barCode;

	private String superVisionCode;

	private int actualPrice;

	private int amount;

	private int socialCategory;

	private String rfsamNo;

	private String psamNo;

	private int tradeRandom;

	private Date tradeTime;

	private String tradeType;

	private int tradeDetailNo;

	@SuppressLint("SimpleDateFormat")
	public TradeDetailMessage(String barCode, String superVisionCode, int actualPrice, int amount, int socialCategory,
			String rfsamNo, String psamNo, int tradeRandom, Date tradeTime, String tradeType, int tradeDetailNo) {
		short bodyLength = (short) (2 + barCode.length() + 2 + superVisionCode.length() + 4 + 2 + 1 + 8 + 6 + 4 + 7 + 1
				+ 4);

		this.barCode = barCode;
		this.superVisionCode = superVisionCode;
		this.actualPrice = actualPrice;
		this.amount = amount;
		this.socialCategory = socialCategory;
		this.rfsamNo = rfsamNo;
		this.psamNo = psamNo;
		this.tradeRandom = tradeRandom;
		this.tradeTime = tradeTime;
		this.tradeType = tradeType;
		this.tradeDetailNo = tradeDetailNo;

		byte[] data = new byte[bodyLength];
		int position = 0;
		System.arraycopy(ByteUtils.intToBytes2(barCode.length()), 0, data, position, 2);
		position += 2;
		System.arraycopy(barCode.getBytes(CHARSET), 0, data, position, barCode.length());
		position += barCode.length();
		System.arraycopy(ByteUtils.intToBytes2(superVisionCode.length()), 0, data, position, 2);
		position += 2;
		System.arraycopy(superVisionCode.getBytes(CHARSET), 0, data, position, superVisionCode.length());
		position += superVisionCode.length();
		System.arraycopy(ByteUtils.intToBytes(actualPrice), 0, data, position, 4);
		position += 4;
		System.arraycopy(ByteUtils.intToBytes2(amount), 0, data, position, 2);
		position += 2;
		data[position] = (byte) socialCategory;
		position += 1;
		System.arraycopy(rfsamNo.getBytes(CHARSET), 0, data, position, 8);
		position += 8;
		System.arraycopy(ByteUtils.hexStr2bytes(psamNo), 0, data, position, 6);
		position += 6;
		System.arraycopy(ByteUtils.intToBytes(tradeRandom), 0, data, position, 4);
		position += 4;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		System.arraycopy(ByteUtils.hexStr2bytes(sdf.format(tradeTime)), 0, data, position, 7);
		position += 7;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeType), 0, data, position, 1);
		position += 1;
		System.arraycopy(ByteUtils.intToBytes(tradeDetailNo), 0, data, position, 4);
		position += 4;

		this.command = SoCommand.上传交易明细;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();

	}

	@Override
	public String toString() {
		return super.toString();
	}

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

	public String getRfsamNo() {
		return rfsamNo;
	}

	public void setRfsamNo(String rfsamNo) {
		this.rfsamNo = rfsamNo;
	}

	public String getPsamNo() {
		return psamNo;
	}

	public void setPsamNo(String psamNo) {
		this.psamNo = psamNo;
	}

	public int getTradeRandom() {
		return tradeRandom;
	}

	public void setTradeRandom(int tradeRandom) {
		this.tradeRandom = tradeRandom;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public int getTradeDetailNo() {
		return tradeDetailNo;
	}

	public void setTradeDetailNo(int tradeDetailNo) {
		this.tradeDetailNo = tradeDetailNo;
	}

}
