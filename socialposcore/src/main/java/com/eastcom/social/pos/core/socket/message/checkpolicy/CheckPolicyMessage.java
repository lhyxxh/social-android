package com.eastcom.social.pos.core.socket.message.checkpolicy;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class CheckPolicyMessage extends SoMessage {

	public CheckPolicyMessage() {

		short bodyLength = 0;

		this.command = SoCommand.请求政策文件基本信息;
		this.total = bodyLength;
		super.computeChecksum();
	}

}
