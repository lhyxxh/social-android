package com.eastcom.social.pos.core.socket.client.mylistener.setheartbeat;

import com.eastcom.social.pos.core.socket.listener.setheartbeat.SetHeartBeatRespListener;
import com.eastcom.social.pos.core.socket.message.setheartbeat.SetHeartBeatRespMessage;

public class MySetHeartBeatRespListener implements SetHeartBeatRespListener {

	@Override
	public void handlerRespMessage(SetHeartBeatRespMessage message) {
		System.out.println("heartbeat resp!");
	}

}
