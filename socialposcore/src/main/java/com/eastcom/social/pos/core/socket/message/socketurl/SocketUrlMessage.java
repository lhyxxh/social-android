package com.eastcom.social.pos.core.socket.message.socketurl;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class SocketUrlMessage extends SoMessage {

	public SocketUrlMessage() {
		short bodyLength = 0;

		this.command = SoCommand.请求修改标牌连接的服务器;
		this.total = bodyLength;
		super.computeChecksum();
	}

}
