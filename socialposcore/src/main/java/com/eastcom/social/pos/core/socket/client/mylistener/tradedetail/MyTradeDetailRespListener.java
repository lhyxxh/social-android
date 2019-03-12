package com.eastcom.social.pos.core.socket.client.mylistener.tradedetail;

import com.eastcom.social.pos.core.socket.listener.tradedetail.TradeDetailRespListener;
import com.eastcom.social.pos.core.socket.message.tradedetail.TradeDetailRespMessage;

public class MyTradeDetailRespListener implements TradeDetailRespListener {

	@Override
	public void handlerRespMessage(TradeDetailRespMessage message) {
		System.out.println("tradedetail resp!");
	}

}
