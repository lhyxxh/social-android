package com.eastcom.social.pos.core.socket.message.checksocialstatusgdzh;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class CheckSocialStatusGdzhMessage extends SoMessage {

	private String socialCardNo;

	public CheckSocialStatusGdzhMessage(String socialCardNo) {
		short bodyLength = (short) (9 );

		this.socialCardNo = socialCardNo;
		byte[] data = new byte[bodyLength];

		int position = 0;
		System.arraycopy(socialCardNo.getBytes(CHARSET), 0, data, position,  9);
		position += 9;

		this.command = SoCommand.查询珠海社保卡状态;
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


}
