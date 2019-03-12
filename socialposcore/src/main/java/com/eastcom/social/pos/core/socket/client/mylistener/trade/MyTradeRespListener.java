package com.eastcom.social.pos.core.socket.client.mylistener.trade;

import com.eastcom.social.pos.core.socket.listener.trade.TradeRespListener;
import com.eastcom.social.pos.core.socket.message.trade.TradeRespMessage;

public class MyTradeRespListener implements TradeRespListener {

	@Override
	public void handlerRespMessage(TradeRespMessage message) {
		System.out.println("trade resp!");
	}

}
