package com.eastcom.social.pos.core.socket.message.checksocialstatussdly;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class CheckSocialStatusSdLyMessage extends SoMessage {

	private String socialCardNo;
	private String password;

	public CheckSocialStatusSdLyMessage(String socialCardNo, String password) {
		short bodyLength = (short) (9 + 2 + password
				.length());

		this.socialCardNo = socialCardNo;
		this.password = password;
		byte[] data = new byte[bodyLength];

		int position = 0;
		System.arraycopy(socialCardNo.getBytes(CHARSET), 0, data, position,  9);
		position += 9;
		System.arraycopy(ByteUtils.intToBytes2(password.length()), 0, data,
				position, 2);
		position += 2;
		System.arraycopy(password.getBytes(CHARSET), 0, data, position, password.length());
		position += password.length();

		this.command = SoCommand.查询临沂社保卡状态;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();
	}

	public String getIdCardNo() {
		return socialCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.socialCardNo = idCardNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
