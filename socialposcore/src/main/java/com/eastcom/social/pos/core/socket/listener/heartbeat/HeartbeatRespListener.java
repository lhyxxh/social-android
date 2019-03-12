package com.eastcom.social.pos.core.socket.listener.heartbeat;

import com.eastcom.social.pos.core.socket.message.heartbeat.HeartbeatRespMessage;

public interface HeartbeatRespListener {

	public void handlerRespMessage(HeartbeatRespMessage message);

}
