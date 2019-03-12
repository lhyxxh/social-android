package com.eastcom.social.pos.core.socket.client.mylistener.smsinfo;

import com.eastcom.social.pos.core.socket.listener.smsinfo.SmsInfoRespListener;
import com.eastcom.social.pos.core.socket.message.smsinfo.SmsInfoRespMessage;

public class MySmsInfoRespListener implements SmsInfoRespListener{

	@Override
	public void handlerRespMessage(SmsInfoRespMessage message) {
		System.out.println("SmsInfo resp!");
	}

}
