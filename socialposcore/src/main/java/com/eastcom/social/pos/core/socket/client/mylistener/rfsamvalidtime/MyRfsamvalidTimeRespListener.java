package com.eastcom.social.pos.core.socket.client.mylistener.rfsamvalidtime;

import com.eastcom.social.pos.core.socket.listener.rfsamvalidtime.RfsamValidTimeRespListener;
import com.eastcom.social.pos.core.socket.message.rfsamvalidtime.RfsamValidTimeRespMessage;

public class MyRfsamvalidTimeRespListener implements RfsamValidTimeRespListener {

	@Override
	public void handlerRespMessage(RfsamValidTimeRespMessage message) {
		System.out.println("rfsamvalid resp!");
	}

}
