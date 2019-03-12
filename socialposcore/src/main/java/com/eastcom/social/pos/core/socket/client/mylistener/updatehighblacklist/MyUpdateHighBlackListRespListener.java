package com.eastcom.social.pos.core.socket.client.mylistener.updatehighblacklist;

import com.eastcom.social.pos.core.socket.listener.updatehighblacklist.UpdateHighBlackListRespListener;
import com.eastcom.social.pos.core.socket.message.updatehighblacklist.UpdateHighBlackListRespMessage;


public class MyUpdateHighBlackListRespListener implements UpdateHighBlackListRespListener {

	@Override
	public void handlerRespMessage(UpdateHighBlackListRespMessage message) {
		byte[] data = message.getData();
		String s_data = new String(data);
		System.out.println("updateblacklist resp!"  + s_data);
	}
	
}
