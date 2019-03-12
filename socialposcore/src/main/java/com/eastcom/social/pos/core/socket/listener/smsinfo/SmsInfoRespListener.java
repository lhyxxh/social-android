package com.eastcom.social.pos.core.socket.listener.smsinfo;

import com.eastcom.social.pos.core.socket.message.smsinfo.SmsInfoRespMessage;

public interface SmsInfoRespListener {

	public void handlerRespMessage(SmsInfoRespMessage message);

}
