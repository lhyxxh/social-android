package com.eastcom.social.pos.core.socket.message.socialinfo;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class SocailInfoMessage extends SoMessage {

	private String socialCardNo;
	private String idCardNo;
	private String name;
	private String bankCardNo;
	private int sex;
	private String issuingDate;
	private String validDate;

	public SocailInfoMessage(String socialCardNo, String idCardNo, String name,
			String bankCardNo, int sex, String issuingDate, String validDate) {
		short bodyLength = (short) (9 + 2 + idCardNo.length() + 2
				+ name.getBytes(CHARSET_GB).length + 2 + bankCardNo.length() + 1 + 4 + 4);

		this.socialCardNo = socialCardNo;
		this.idCardNo = idCardNo;
		this.name = name;
		this.bankCardNo = bankCardNo;
		this.sex = sex;
		this.issuingDate = issuingDate;
		this.validDate = validDate;

		byte[] data = new byte[bodyLength];

		int position = 0;
		System.arraycopy(socialCardNo.getBytes(CHARSET), 0, data, position, 9);
		position += 9;
		System.arraycopy(ByteUtils.intToBytes2(idCardNo.length()), 0, data,
				position, 2);
		position += 2;
		System.arraycopy(idCardNo.getBytes(CHARSET), 0, data, position,
				idCardNo.length());
		position += idCardNo.length();
		System.arraycopy(ByteUtils.intToBytes2(name.getBytes(CHARSET_GB).length), 0, data,
				position, 2);
		position += 2;
		System.arraycopy(name.getBytes(CHARSET_GB), 0, data, position,
				name.getBytes(CHARSET_GB).length);
		position += name.getBytes(CHARSET_GB).length;
		System.arraycopy(ByteUtils.intToBytes2(bankCardNo.length()), 0, data,
				position, 2);
		position += 2;
		System.arraycopy(bankCardNo.getBytes(CHARSET), 0, data, position,
				bankCardNo.length());
		position += bankCardNo.length();
		data[position] = (byte) sex;
		position += 1;
		System.arraycopy(ByteUtils.hexStr2bytes(issuingDate), 0, data, position, 4);
		position += 4;
		System.arraycopy(ByteUtils.hexStr2bytes(validDate), 0, data, position, 4);
		position += 4;

		this.command = SoCommand.提交社保卡信息;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();
	}

	public String getSocialCardNo() {
		return socialCardNo;
	}

	public void setSocialCardNo(String socialCardNo) {
		this.socialCardNo = socialCardNo;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getIssuingDate() {
		return issuingDate;
	}

	public void setIssuingDate(String issuingDate) {
		this.issuingDate = issuingDate;
	}

	public String getValidDate() {
		return validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

}
