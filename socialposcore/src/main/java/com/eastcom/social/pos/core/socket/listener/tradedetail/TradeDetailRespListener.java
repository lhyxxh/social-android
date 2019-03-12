package com.eastcom.social.pos.core.socket.listener.tradedetail;

import com.eastcom.social.pos.core.socket.message.tradedetail.TradeDetailRespMessage;

public interface TradeDetailRespListener {

	public void handlerRespMessage(TradeDetailRespMessage message);

}
