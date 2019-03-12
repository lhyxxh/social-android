package com.eastcom.social.pos.core.socket.message.rfsamvalidtime;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class RfsamValidTimeMessage extends SoMessage {

	public RfsamValidTimeMessage() {
		short bodyLength = 0;

		this.command = SoCommand.请求RFSAM默认有效时长;
		this.total = bodyLength;
		super.computeChecksum();
	}

}
