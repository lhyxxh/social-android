package com.eastcom.social.pos.core.socket.client.mylistener.summitcardlog;

import com.eastcom.social.pos.core.socket.listener.summitcardlog.SummitCardLogRespListener;
import com.eastcom.social.pos.core.socket.message.summitcardlog.SummitCardLogRespMessage;

public class MySummitCardlogRespListener implements SummitCardLogRespListener {

	@Override
	public void handlerRespMessage(SummitCardLogRespMessage message) {
		System.out.println("changepwd resp!");
	}

}
