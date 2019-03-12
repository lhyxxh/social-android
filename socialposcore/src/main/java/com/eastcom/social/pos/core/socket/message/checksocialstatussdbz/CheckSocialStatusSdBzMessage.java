package com.eastcom.social.pos.core.socket.message.checksocialstatussdbz;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class CheckSocialStatusSdBzMessage extends SoMessage {

	private String terminalCode;

	private String socialCardNo;

	private String bankBinCode;

	private String idCardNo;
	

	public CheckSocialStatusSdBzMessage(String terminalCode, String socialCardNo, String bankBinCode, String idCardNo) {
		short bodyLength = (short) (4 + 9 + 3 + 2 + idCardNo.length());

		this.terminalCode = terminalCode;
		this.socialCardNo = socialCardNo;
		this.bankBinCode = bankBinCode;
		this.idCardNo = idCardNo;

		byte[] data = new byte[bodyLength];

		int position = 0;
		System.arraycopy(ByteUtils.hexStr2bytes(terminalCode), 0, data, position, 4);
		position += 4;
		System.arraycopy(socialCardNo.getBytes(CHARSET), 0, data, position, 9);
		position += 9;
		System.arraycopy(ByteUtils.hexStr2bytes(bankBinCode), 0, data, position, 3);
		position += 3;
		System.arraycopy(ByteUtils.intToBytes2(idCardNo.length()), 0, data, position, 2);
		position += 2;
		System.arraycopy(idCardNo.getBytes(CHARSET), 0, data, position, idCardNo.length());
		position += idCardNo.length();

		this.command = SoCommand.查询社保卡状态;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();
	}

	public String getTerminalCode() {
		return terminalCode;
	}

	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	public String getSocialCardNo() {
		return socialCardNo;
	}

	public void setSocialCardNo(String socialCardNo) {
		this.socialCardNo = socialCardNo;
	}

	public String getBankBinCode() {
		return bankBinCode;
	}

	public void setBankBinCode(String bankBinCode) {
		this.bankBinCode = bankBinCode;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}


}
