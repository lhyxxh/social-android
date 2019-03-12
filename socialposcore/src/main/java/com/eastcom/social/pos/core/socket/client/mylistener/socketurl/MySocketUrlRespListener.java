package com.eastcom.social.pos.core.socket.client.mylistener.socketurl;

import com.eastcom.social.pos.core.socket.listener.socketurl.SocketUrlRespListener;
import com.eastcom.social.pos.core.socket.message.socketurl.SocketUrlRespMessage;

public class MySocketUrlRespListener implements SocketUrlRespListener {

	@Override
	public void handlerRespMessage(SocketUrlRespMessage message) {
		System.out.println("socketurl resp!");
	}

}
