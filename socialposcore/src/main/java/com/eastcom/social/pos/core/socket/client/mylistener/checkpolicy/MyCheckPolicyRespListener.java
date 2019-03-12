package com.eastcom.social.pos.core.socket.client.mylistener.checkpolicy;

import com.eastcom.social.pos.core.socket.listener.checkpolicy.CheckPolicyRespListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.checkpolicy.CheckPolicyRespMessage;

public class MyCheckPolicyRespListener implements CheckPolicyRespListener {


	@Override	
	public void handlerRespMessage(SoMessage message) {
		System.out.println("CheckPolicy resp!");
	}


}
