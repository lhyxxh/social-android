package com.eastcom.social.pos.core.socket.client.mylistener.checkhighblacklist;

import com.eastcom.social.pos.core.socket.listener.checkhighblacklist.CheckHighBlackListRespListener;
import com.eastcom.social.pos.core.socket.message.checkhighblacklist.CheckHighBlackListRespMessage;


public class MyCheckHighBlackListRespListener implements CheckHighBlackListRespListener {

	@Override
	public void handlerRespMessage(CheckHighBlackListRespMessage message) {
		System.out.println("checkhighblacklist resp!");
	}

	
}
