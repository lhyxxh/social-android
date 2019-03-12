package com.eastcom.social.pos.core.socket.listener.checkversion;

import com.eastcom.social.pos.core.socket.message.checkversion.CheckVersionRespMessage;

public interface CheckVersionRespListener {

	public void handlerRespMessage(CheckVersionRespMessage message);

}
