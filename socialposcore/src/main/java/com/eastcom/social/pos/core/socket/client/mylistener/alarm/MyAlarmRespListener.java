package com.eastcom.social.pos.core.socket.client.mylistener.alarm;

import com.eastcom.social.pos.core.socket.listener.alarm.AlarmRespListener;
import com.eastcom.social.pos.core.socket.message.alarm.AlarmRespMessage;

public class MyAlarmRespListener implements AlarmRespListener{

	@Override
	public void handlerRespMessage(AlarmRespMessage message) {
		// TODO Auto-generated method stub
		System.out.println("alarm resp!");
	}

}
