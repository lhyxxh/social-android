package com.eastcom.social.pos.core.socket.message.healthproduct;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class HealthProductRespMessage extends SoMessage {
	private String barCode;

	public HealthProductRespMessage(SoMessage message) throws Exception {
		super(message);

		int barCodeLength = ByteUtils.bytes2Int(this.body, 0, 2);
		this.setBarCode(new String(this.body, 2, barCodeLength, CHARSET));
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

}
