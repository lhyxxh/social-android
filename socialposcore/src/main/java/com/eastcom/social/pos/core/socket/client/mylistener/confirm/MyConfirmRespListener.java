package com.eastcom.social.pos.core.socket.client.mylistener.confirm;

import com.eastcom.social.pos.core.socket.listener.confirm.ConfirmRespListener;
import com.eastcom.social.pos.core.socket.message.confirm.ConfirmRespMessage;

public class MyConfirmRespListener implements ConfirmRespListener {

	@Override
	public void handlerRespMessage(ConfirmRespMessage message) {
		System.out.println("confirm resp!");
	}

}
