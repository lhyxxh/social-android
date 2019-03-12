package com.eastcom.social.pos.core.socket.client.mylistener.checksocialstatus;

import com.eastcom.social.pos.core.socket.listener.checksocialstatus.CheckSocialStatusRespListener;
import com.eastcom.social.pos.core.socket.message.checksocialstatus.CheckSocialStatusRespMessage;


public class MyCheckSocialStatusRespListener implements CheckSocialStatusRespListener {

	@Override
	public void handlerRespMessage(CheckSocialStatusRespMessage message) {
		System.out.println("checksocialstatusrespmessage resp!");
	}


	
}
