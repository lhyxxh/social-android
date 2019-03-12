package com.eastcom.social.pos.core.socket.listener.rfsamvalidtime;

import com.eastcom.social.pos.core.socket.message.rfsamvalidtime.RfsamValidTimeRespMessage;

public interface RfsamValidTimeRespListener {

	public void handlerRespMessage(RfsamValidTimeRespMessage message);

}
