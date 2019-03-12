package com.eastcom.social.pos.core.socket.message.shType;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class ShTypeRespMessage extends SoMessage {

	private int shType;

	public ShTypeRespMessage(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		setShType(ByteUtils.bytes2Int(body));
	}

	public int getShType() {
		return shType;
	}

	public void setShType(int shType) {
		this.shType = shType;
	}



}
