package com.eastcom.social.pos.core.socket.message.shType;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class ShTypeMessage extends SoMessage {

	public ShTypeMessage() {
		short bodyLength = 0;

		this.command = SoCommand.定点机构类型;
		this.total = bodyLength;
		super.computeChecksum();
	}

}
