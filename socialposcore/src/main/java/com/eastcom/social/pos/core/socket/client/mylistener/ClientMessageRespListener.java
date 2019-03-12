package com.eastcom.social.pos.core.socket.client.mylistener;

import com.eastcom.social.pos.core.socket.listener.MessageRespListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class ClientMessageRespListener implements MessageRespListener {

	@Override
	public void handlerRespMessage(SoMessage message) {
		System.out.println("msg resp!");
	}

}
