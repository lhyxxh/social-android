package com.eastcom.social.pos.core.socket.message.queryhealth;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class QueryHealthMessage extends SoMessage {

	private String barCode;

	public QueryHealthMessage(String barCode) {
		short bodyLength = (short) (2 + barCode.length());

		this.setBarCode(barCode);

		byte[] data = new byte[bodyLength];

		System.arraycopy(ByteUtils.intToBytes2((bodyLength - 2)), 0, data, 0, 2);
		System.arraycopy(barCode.getBytes(CHARSET), 0, data, 2, bodyLength - 2);

		this.command = SoCommand.查询保健品信息;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();

	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

}
