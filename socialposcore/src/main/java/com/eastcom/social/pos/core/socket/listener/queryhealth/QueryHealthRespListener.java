package com.eastcom.social.pos.core.socket.listener.queryhealth;

import com.eastcom.social.pos.core.socket.message.queryhealth.QueryHealthRespMessage;

public interface QueryHealthRespListener {

	public void handlerRespMessage(QueryHealthRespMessage message);

}
