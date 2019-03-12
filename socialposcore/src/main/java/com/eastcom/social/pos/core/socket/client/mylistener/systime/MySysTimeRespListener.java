package com.eastcom.social.pos.core.socket.client.mylistener.systime;

import com.eastcom.social.pos.core.socket.listener.systime.SysTimeRespListener;
import com.eastcom.social.pos.core.socket.message.systime.SysTimeRespMessage;

public class MySysTimeRespListener implements SysTimeRespListener{

	@Override
	public void handlerRespMessage(SysTimeRespMessage message) {
		System.out.println("SysTimeRespMessage resp!");
	}

}
