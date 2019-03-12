package com.eastcom.social.pos.core.socket.message.smsinfo;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class SmsInfoMessage extends SoMessage {

	public SmsInfoMessage() {
		short bodyLength = 0;

		this.command = SoCommand.获取系统短通知;
		this.total = bodyLength;

		super.computeChecksum();
	}

}
