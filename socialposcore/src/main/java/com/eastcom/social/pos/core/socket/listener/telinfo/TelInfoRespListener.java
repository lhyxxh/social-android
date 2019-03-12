package com.eastcom.social.pos.core.socket.listener.telinfo;

import com.eastcom.social.pos.core.socket.message.telinfo.TelInfoRespMessage;


public interface TelInfoRespListener {

	public void handlerRespMessage(TelInfoRespMessage message);

}
