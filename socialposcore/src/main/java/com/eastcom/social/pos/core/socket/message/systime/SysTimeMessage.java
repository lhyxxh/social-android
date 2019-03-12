package com.eastcom.social.pos.core.socket.message.systime;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class SysTimeMessage extends SoMessage {

	public SysTimeMessage() {
		short bodyLength = 0;

		this.command = SoCommand.获取系统时间;
		this.total = bodyLength;

		super.computeChecksum();
	}

}
