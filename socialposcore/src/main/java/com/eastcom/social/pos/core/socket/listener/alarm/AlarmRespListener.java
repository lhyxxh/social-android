package com.eastcom.social.pos.core.socket.listener.alarm;

import com.eastcom.social.pos.core.socket.message.alarm.AlarmRespMessage;

public interface AlarmRespListener {

	public void handlerRespMessage(AlarmRespMessage message);

}
