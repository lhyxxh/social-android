package com.eastcom.social.pos.core.socket.client.mylistener.checksocialstatussdly;

import com.eastcom.social.pos.core.socket.listener.checksocialstatussdly.CheckSocialStatusSdLyRespListener;
import com.eastcom.social.pos.core.socket.message.checksocialstatussdly.CheckSocialStatusSdLyRespMessage;


public class MyCheckSocialStatusSdLyRespListener implements CheckSocialStatusSdLyRespListener {

	@Override
	public void handlerRespMessage(CheckSocialStatusSdLyRespMessage message) {
		System.out.println("checksocialstatussdlyrespmessage resp!");
	}

	
}
