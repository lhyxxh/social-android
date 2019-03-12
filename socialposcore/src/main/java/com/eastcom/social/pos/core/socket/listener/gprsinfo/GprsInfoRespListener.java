package com.eastcom.social.pos.core.socket.listener.gprsinfo;

import com.eastcom.social.pos.core.socket.message.gprsinfo.GprsInfoRespMessage;


public interface GprsInfoRespListener {

	public void handlerRespMessage(GprsInfoRespMessage message);

}
