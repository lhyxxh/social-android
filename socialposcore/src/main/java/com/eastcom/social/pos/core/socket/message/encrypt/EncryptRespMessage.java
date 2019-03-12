package com.eastcom.social.pos.core.socket.message.encrypt;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class EncryptRespMessage extends SoMessage {

	private int is_encrypt;

	public EncryptRespMessage(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		setIs_encrypt(ByteUtils.bytes2Int(body));
	}

	public int getIs_encrypt() {
		return is_encrypt;
	}

	public void setIs_encrypt(int is_encrypt) {
		this.is_encrypt = is_encrypt;
	}


}
