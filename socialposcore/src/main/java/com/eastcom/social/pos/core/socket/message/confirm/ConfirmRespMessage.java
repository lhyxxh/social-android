package com.eastcom.social.pos.core.socket.message.confirm;

import com.eastcom.social.pos.core.socket.message.SoMessage;

public class ConfirmRespMessage extends SoMessage {

	private int success;

	public ConfirmRespMessage(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		this.success = (int) body[0];
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

}
