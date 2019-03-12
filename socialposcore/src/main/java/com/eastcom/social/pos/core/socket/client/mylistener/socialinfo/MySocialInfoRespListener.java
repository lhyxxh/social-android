package com.eastcom.social.pos.core.socket.client.mylistener.socialinfo;

import com.eastcom.social.pos.core.socket.listener.socialinfo.SocialInfoRespListener;
import com.eastcom.social.pos.core.socket.message.socialinfo.SocailInfoRespMessage;

public class MySocialInfoRespListener implements SocialInfoRespListener {

	@Override
	public void handlerRespMessage(SocailInfoRespMessage message) {
		System.out.println("socialinfo resp!");
		
	}


}
