package com.eastcom.social.pos.core.socket.message.systime;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class SysTimeRespMessage extends SoMessage {

	private String sysTime;

	public SysTimeRespMessage(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		sysTime = ByteUtils.bcd2Str(body, 0, 7);
	}

	public String getSysTime() {
		return sysTime;
	}

	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}

}
