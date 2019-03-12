package com.eastcom.social.pos.core.socket.client.mylistener.rfsamlist;

import com.eastcom.social.pos.core.socket.listener.rfsamlist.RfsamListRespListener;
import com.eastcom.social.pos.core.socket.message.rfsamlist.RfsamListRespMessage;

public class MyRfsamListRespListener implements RfsamListRespListener {

	@Override
	public void handlerRespMessage(RfsamListRespMessage message) {
		System.out.println("rfsamlis resp!");
	}

}
