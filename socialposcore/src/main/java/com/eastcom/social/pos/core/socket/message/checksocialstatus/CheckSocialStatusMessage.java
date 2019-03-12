package com.eastcom.social.pos.core.socket.message.checksocialstatus;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class CheckSocialStatusMessage extends SoMessage {

	private String socialCardNo;

	public CheckSocialStatusMessage(String socialCardNo) {
		short bodyLength = 9;

		this.socialCardNo = socialCardNo;

		byte[] data = new byte[bodyLength];
		int position = 0;
		System.arraycopy(socialCardNo.getBytes(CHARSET), 0, data, position, socialCardNo.length());
		position += 9;

		this.command = SoCommand.查询社保卡状态;
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

}
