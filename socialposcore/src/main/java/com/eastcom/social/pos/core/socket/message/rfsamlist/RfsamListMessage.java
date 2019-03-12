package com.eastcom.social.pos.core.socket.message.rfsamlist;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class RfsamListMessage extends SoMessage {

	public RfsamListMessage() {
		short bodyLength = 0;

		this.command = SoCommand.请求RFSAM卡名单;
		this.total = bodyLength;
		super.computeChecksum();
	}

}
