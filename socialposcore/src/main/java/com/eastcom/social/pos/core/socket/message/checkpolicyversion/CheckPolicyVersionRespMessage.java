package com.eastcom.social.pos.core.socket.message.checkpolicyversion;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class CheckPolicyVersionRespMessage extends SoMessage {

	private int result;

	public CheckPolicyVersionRespMessage(SoMessage message) {
		super(message);

		this.result = ByteUtils.bytes2Int(this.body, 0, 1);
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

}
