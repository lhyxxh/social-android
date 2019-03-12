package com.eastcom.social.pos.core.socket.message.encrypt;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class EncryptMessage extends SoMessage {

	public EncryptMessage() {
		short bodyLength = 0;

		this.command = SoCommand.请求交易数据是否被加密;
		this.total = bodyLength;
		super.computeChecksum();
	}

}
