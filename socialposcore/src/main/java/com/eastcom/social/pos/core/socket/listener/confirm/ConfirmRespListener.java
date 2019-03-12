package com.eastcom.social.pos.core.socket.listener.confirm;

import com.eastcom.social.pos.core.socket.message.confirm.ConfirmRespMessage;

public interface ConfirmRespListener {

	public void handlerRespMessage(ConfirmRespMessage message);

}
