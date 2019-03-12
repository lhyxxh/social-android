package com.eastcom.social.pos.core.socket.client.mylistener.updatesoft;

import com.eastcom.social.pos.core.socket.listener.updatesoft.UpdateSoftRespListener;
import com.eastcom.social.pos.core.socket.message.updatesoft.UpdateSoftRespMessage;

public class MyUpdateSoftRespListener implements UpdateSoftRespListener {

	@Override
	public void handlerRespMessage(UpdateSoftRespMessage message) {
		System.out.println("updatesoft resp!");
	}

}
