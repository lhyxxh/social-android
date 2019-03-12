	package com.eastcom.social.pos.core.socket.message.checksocialstatussdbz;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class CheckSocialStatusSdBzRespMessage extends SoMessage {

	private int isSuccess;

	private int balanceValue;
	
	private int status;

	private String socialCardNo;
	
	private String tel;
	
	public CheckSocialStatusSdBzRespMessage(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		this.isSuccess = ByteUtils.bytes2Int(body, 0,1);
		balanceValue = ByteUtils.bytes2Int(body, 1, 4);
		status = ByteUtils.bytes2Int(body, 5,1);
		socialCardNo = new String(this.body, 6, 9, CHARSET);
		tel = ByteUtils.bcd2Str(body, 15, 6);
	}

	public int getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(int isSuccess) {
		this.isSuccess = isSuccess;
	}

	public int getBalanceValue() {
		return balanceValue;
	}

	public void setBalanceValue(int balanceValue) {
		this.balanceValue = balanceValue;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSocialCardNo() {
		return socialCardNo;
	}

	public void setSocialCardNo(String socialCardNo) {
		this.socialCardNo = socialCardNo;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

//	public int getIsSuccessBalance() {
//		return isSuccessBalance;
//	}
//
//	public void setIsSuccessBalance(int isSuccessBalance) {
//		this.isSuccessBalance = isSuccessBalance;
//	}
	
	

}
