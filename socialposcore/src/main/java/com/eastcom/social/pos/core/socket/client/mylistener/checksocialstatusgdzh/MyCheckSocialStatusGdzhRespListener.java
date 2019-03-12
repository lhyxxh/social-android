package com.eastcom.social.pos.core.socket.client.mylistener.checksocialstatusgdzh;

import com.eastcom.social.pos.core.socket.listener.checksocialstatusgdzh.CheckSocialStatusGdzhRespListener;
import com.eastcom.social.pos.core.socket.message.checksocialstatusgdzh.CheckSocialStatusGdzhRespMessage;


public class MyCheckSocialStatusGdzhRespListener implements CheckSocialStatusGdzhRespListener {

	@Override
	public void handlerRespMessage(CheckSocialStatusGdzhRespMessage message) {
		System.out.println("checkSocialStatusGdzhRespMessage resp!");
		
	}

}
