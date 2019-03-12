package com.eastcom.social.pos.core.socket.message.trade;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

import android.annotation.SuppressLint;

public class SocialTradeMessage extends SoMessage {

	private String tradeType;

	private String tradeState;

	private String isEncrypt;

	private int preNo;

	private String bankCard;

	private String socialCardNo;

	private String refNo;

	private Date tradeTime;

	private String tradeSn;

	private int tradeMoney;

	private String psamNo;

	private String terminalCode;

	private String rfsamNo;

	private int tradeRandom;

	private int mac;

	private int sendCount;

	private int tradeNo;

	private String payType;

	@SuppressLint("SimpleDateFormat")
	public SocialTradeMessage(String tradeType, String tradeState,
			String isEncrypt, int preNo, String bankCard, String socialCardNo,
			String refNo, Date tradeTime, String tradeSn, int tradeMoney,
			String psamNo, String terminalCode, String rfsamNo,
			int tradeRandom, int mac, int sendCount, int tradeNo) {
		short bodyLength = 97;

		this.tradeType = tradeType;
		this.tradeState = tradeState;
		this.isEncrypt = isEncrypt;
		this.preNo = preNo;
		this.bankCard = bankCard;
		this.socialCardNo = socialCardNo;
		this.refNo = refNo;
		this.tradeTime = tradeTime;
		this.tradeSn = tradeSn;
		this.tradeMoney = tradeMoney;
		this.psamNo = psamNo;
		this.terminalCode = terminalCode;
		this.rfsamNo = rfsamNo;
		this.tradeRandom = tradeRandom;
		this.mac = mac;
		this.sendCount = sendCount;
		this.tradeNo = tradeNo;

		byte[] data = new byte[bodyLength];
		int position = 0;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeType), 0, data, position,
				1);
		position += 1;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeState), 0, data, position,
				2);
		position += 2;
		System.arraycopy(ByteUtils.hexStr2bytes(isEncrypt), 0, data, position,
				1);
		position += 1;
		System.arraycopy(ByteUtils.intToBytes(preNo), 0, data, position, 4);
		position += 4;
		position += 2;
		System.arraycopy(ByteUtils.hexStr2bytes(bankCard), 0, data, position,
				10);
		position += 10;
		// 社保卡填充
		data[position] = (byte) 0xad;
		position += 5;
		position += 1;
		System.arraycopy(ByteUtils.hexStr2bytes(refNo), 0, data, position, 6);
		position += 6;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		System.arraycopy(ByteUtils.hexStr2bytes(sdf.format(tradeTime)), 0,
				data, position, 7);
		position += 7;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeSn), 0, data, position, 3);
		position += 3;
		System.arraycopy(ByteUtils.intToBytes(tradeMoney), 0, data, position, 4);
		position += 4;
		// 修改psam卡号，截取前6位，后6位
		StringBuffer sb = new StringBuffer();
		sb.append(psamNo.substring(0, 6));
		sb.append(psamNo.substring(psamNo.length() - 6, psamNo.length()));
		System.arraycopy(ByteUtils.hexStr2bytes(sb.toString()), 0, data,
				position, 6);
		position += 6;
		System.arraycopy(ByteUtils.hexStr2bytes(terminalCode), 0, data,
				position, 4);
		position += 4;
		System.arraycopy(rfsamNo.getBytes(CHARSET), 0, data, position, 8);
		position += 8;
		System.arraycopy(socialCardNo.getBytes(CHARSET), 0, data, position, 9);
		position += 9;
		position += 1;
		System.arraycopy(ByteUtils.intToBytes(tradeRandom), 0, data, position,
				4);
		position += 4;
		System.arraycopy(ByteUtils.intToBytes(mac), 0, data, position, 4);
		position += 4;
		System.arraycopy(rfsamNo.getBytes(CHARSET), 0, data, position, 8);
		position += 8;
		System.arraycopy(ByteUtils.intToBytes(tradeRandom), 0, data, position,
				4);
		position += 4;
		data[position] = (byte) sendCount;
		position += 1;
		System.arraycopy(ByteUtils.intToBytes2(tradeNo), 0, data, position, 2);
		position += 2;

		this.command = SoCommand.上传交易;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();
	}

	public SocialTradeMessage(String tradeType, String tradeState,
			String isEncrypt, int preNo, String rfsamNo, int tradeRandom,
			int mac, int sendCount, int tradeNo, String cipherText) {
		short bodyLength = 97;

		this.tradeType = tradeType;
		this.tradeState = tradeState;
		this.isEncrypt = isEncrypt;
		this.preNo = preNo;
		this.rfsamNo = rfsamNo;
		this.tradeRandom = tradeRandom;
		this.mac = mac;
		this.sendCount = sendCount;
		this.tradeNo = tradeNo;

		byte[] data = new byte[bodyLength];
		int position = 0;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeType), 0, data, position,
				1);
		position += 1;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeState), 0, data, position,
				2);
		position += 2;
		System.arraycopy(ByteUtils.hexStr2bytes(isEncrypt), 0, data, position,
				1);
		position += 1;
		System.arraycopy(ByteUtils.intToBytes(preNo), 0, data, position, 4);
		position += 4;
		position += 2;
		System.arraycopy(ByteUtils.hexStr2bytes(cipherText), 0, data, position,
				64);
		position += 64;
		System.arraycopy(ByteUtils.intToBytes(tradeRandom), 0, data, position,
				4);
		position += 4;
		System.arraycopy(ByteUtils.intToBytes(mac), 0, data, position, 4);
		position += 4;
		System.arraycopy(rfsamNo.getBytes(CHARSET), 0, data, position, 8);
		position += 8;
		System.arraycopy(ByteUtils.intToBytes(tradeRandom), 0, data, position,
				4);
		position += 4;
		data[position] = (byte) sendCount;
		position += 1;
		System.arraycopy(ByteUtils.intToBytes2(tradeNo), 0, data, position, 2);
		position += 2;

		this.command = SoCommand.上传交易;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();
	}

	@SuppressLint("SimpleDateFormat")
	public static String getCipherText(String bandCardNo, String socialCardNo,
			String refNo, Date tradeTime, String tradeSn, int tradeMoney,
			String psamNo, String terminalCode, String rfsamNo) {
		StringBuffer buffer = new StringBuffer("");
		buffer.append(bandCardNo);
		buffer.append("ad");
		buffer.append(ByteUtils.bcd2Str(socialCardNo.substring(0, 1).getBytes(
				CHARSET)));
		buffer.append(ByteUtils.bcd2Str(ByteUtils.intToBytes3(Integer
				.valueOf(socialCardNo.substring(1, 8)))));
		buffer.append(ByteUtils.bcd2Str(socialCardNo.substring(8, 9).getBytes(
				CHARSET)));
		buffer.append(refNo);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		buffer.append(sdf.format(tradeTime));
		buffer.append(tradeSn);
		String money = ByteUtils.bcd2Str(ByteUtils.intToBytes(tradeMoney));
		buffer.append(money);
		// 修改psam卡号，截取前6位，后6位
		StringBuffer sb = new StringBuffer();
		sb.append(psamNo.substring(0, 6));
		sb.append(psamNo.substring(psamNo.length() - 6, psamNo.length()));
		buffer.append(sb.toString());
		buffer.append(terminalCode);
		String rfsam = ByteUtils.bcd2Str(rfsamNo.getBytes(CHARSET));
		buffer.append(rfsam);
		buffer.append(ByteUtils.bcd2Str(socialCardNo.getBytes(CHARSET)));
		buffer.append("00");
		return buffer.toString();
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

	public String getIsEncrypt() {
		return isEncrypt;
	}

	public void setIsEncrypt(String isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

	public int getPreNo() {
		return preNo;
	}

	public void setPreNo(int preNo) {
		this.preNo = preNo;
	}

	public String getBankCard() {
		return bankCard;
	}

	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
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

	public int getTradeRandom() {
		return tradeRandom;
	}

	public void setTradeRandom(int tradeRandom) {
		this.tradeRandom = tradeRandom;
	}

	public int getMac() {
		return mac;
	}

	public void setMac(int mac) {
		this.mac = mac;
	}

	public int getSendCount() {
		return sendCount;
	}

	public void setSendCount(int sendCount) {
		this.sendCount = sendCount;
	}

	public int getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(int tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

}
