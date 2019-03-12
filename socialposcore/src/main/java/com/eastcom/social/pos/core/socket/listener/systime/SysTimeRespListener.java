package com.eastcom.social.pos.core.socket.listener.systime;

import com.eastcom.social.pos.core.socket.message.systime.SysTimeRespMessage;

public interface SysTimeRespListener {

	public void handlerRespMessage(SysTimeRespMessage message);

}
