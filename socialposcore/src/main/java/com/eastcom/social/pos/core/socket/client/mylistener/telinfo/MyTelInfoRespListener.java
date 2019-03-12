package com.eastcom.social.pos.core.socket.client.mylistener.telinfo;

import com.eastcom.social.pos.core.socket.listener.telinfo.TelInfoRespListener;
import com.eastcom.social.pos.core.socket.message.telinfo.TelInfoRespMessage;

public class MyTelInfoRespListener implements TelInfoRespListener {


	@Override
	public void handlerRespMessage(TelInfoRespMessage message) {
		System.out.println("TelInfoRespMessage resp!");		
	}


}
