package com.eastcom.social.pos.core.socket.client.mylistener.checkversion;

import com.eastcom.social.pos.core.socket.listener.checkversion.CheckVersionRespListener;
import com.eastcom.social.pos.core.socket.message.checkversion.CheckVersionRespMessage;

public class MyCheckVersionRespListener implements CheckVersionRespListener {

	@Override
	public void handlerRespMessage(CheckVersionRespMessage message) {
		System.out.println("checkversion resp!");
	}

}
