package com.eastcom.social.pos.core.socket.message.rfsamvalidtime;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class RfsamValidTimeRespMessage extends SoMessage {

	private int rfsamValidTime;

	public RfsamValidTimeRespMessage(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		setRfsamValidTime(ByteUtils.bytes2Int(body));
	}

	public int getRfsamValidTime() {
		return rfsamValidTime;
	}

	public void setRfsamValidTime(int rfsamValidTime) {
		this.rfsamValidTime = rfsamValidTime;
	}

}
