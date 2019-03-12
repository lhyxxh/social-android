package com.eastcom.social.pos.core.socket.listener.setheartbeat;

import com.eastcom.social.pos.core.socket.message.setheartbeat.SetHeartBeatRespMessage;

public interface SetHeartBeatRespListener {

	public void handlerRespMessage(SetHeartBeatRespMessage message);

}
