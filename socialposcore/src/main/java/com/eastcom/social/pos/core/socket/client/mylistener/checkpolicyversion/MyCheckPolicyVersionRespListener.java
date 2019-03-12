package com.eastcom.social.pos.core.socket.client.mylistener.checkpolicyversion;

import com.eastcom.social.pos.core.socket.listener.checkpolicyversion.CheckPolicyVersionRespListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class MyCheckPolicyVersionRespListener implements CheckPolicyVersionRespListener {


	@Override
	public void handlerRespMessage(SoMessage message) {
		System.out.println("CheckPolicyVersionRespMessage resp!");
	}

}
