package com.eastcom.social.pos.core.socket.client.mylistener.gprsinfo;

import com.eastcom.social.pos.core.socket.listener.gprsinfo.GprsInfoRespListener;
import com.eastcom.social.pos.core.socket.message.gprsinfo.GprsInfoRespMessage;

public class MyGprsInfoRespListener implements GprsInfoRespListener {

	@Override
	public void handlerRespMessage(GprsInfoRespMessage message) {
		System.out.println("gprsinfo resp!");
		
	}


}
