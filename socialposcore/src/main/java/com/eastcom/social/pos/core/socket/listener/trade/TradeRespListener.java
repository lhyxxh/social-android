package com.eastcom.social.pos.core.socket.listener.trade;

import com.eastcom.social.pos.core.socket.message.trade.TradeRespMessage;

public interface TradeRespListener {

	public void handlerRespMessage(TradeRespMessage message);

}
