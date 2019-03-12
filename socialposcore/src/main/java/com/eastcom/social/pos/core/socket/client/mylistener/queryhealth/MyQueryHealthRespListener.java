package com.eastcom.social.pos.core.socket.client.mylistener.queryhealth;

import com.eastcom.social.pos.core.socket.listener.queryhealth.QueryHealthRespListener;
import com.eastcom.social.pos.core.socket.message.queryhealth.QueryHealthRespMessage;

public class MyQueryHealthRespListener implements QueryHealthRespListener {

	@Override
	public void handlerRespMessage(QueryHealthRespMessage message) {
		System.out.println("queryhealth resp!");
	}

}
