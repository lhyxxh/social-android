package com.eastcom.social.pos.core.socket.client.mylistener.checksocialstatussdbz;

import com.eastcom.social.pos.core.socket.listener.checksocialstatussdbz.CheckSocialStatusSdBzRespListener;
import com.eastcom.social.pos.core.socket.message.checksocialstatussdbz.CheckSocialStatusSdBzRespMessage;


public class MyCheckSocialStatusSdBzRespListener implements CheckSocialStatusSdBzRespListener {

	@Override
	public void handlerRespMessage(CheckSocialStatusSdBzRespMessage message) {
		System.out.println("checksocialstatussdbzrespmessage resp!");
	}
	
}
