package com.eastcom.social.pos.core.socket.message.trade;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class TradeRespMessage extends SoMessage {

	private int tradeRandom;

	public TradeRespMessage(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		this.setTradeRandom(ByteUtils.bytes2Int(body));
	}

	public int getTradeRandom() {
		return tradeRandom;
	}

	public void setTradeRandom(int tradeRandom) {
		this.tradeRandom = tradeRandom;
	}

}
