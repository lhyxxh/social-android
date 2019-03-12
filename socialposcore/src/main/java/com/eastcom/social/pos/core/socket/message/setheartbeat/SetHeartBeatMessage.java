package com.eastcom.social.pos.core.socket.message.setheartbeat;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class SetHeartBeatMessage extends SoMessage {

	public SetHeartBeatMessage() {
		short bodyLength = 0;

		this.command = SoCommand.请求心跳频率;
		this.total = bodyLength;
		super.computeChecksum();
	}

}
