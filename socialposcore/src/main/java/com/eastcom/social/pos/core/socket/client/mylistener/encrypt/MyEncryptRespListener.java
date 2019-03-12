package com.eastcom.social.pos.core.socket.client.mylistener.encrypt;

import com.eastcom.social.pos.core.socket.listener.encrypt.EncryptRespListener;
import com.eastcom.social.pos.core.socket.message.encrypt.EncryptRespMessage;

public class MyEncryptRespListener implements EncryptRespListener {

	@Override
	public void handlerRespMessage(EncryptRespMessage message) {
		System.out.println("encrypt resp!");
	}

}
