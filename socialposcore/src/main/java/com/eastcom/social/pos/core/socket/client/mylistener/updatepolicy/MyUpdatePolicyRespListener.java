package com.eastcom.social.pos.core.socket.client.mylistener.updatepolicy;

import com.eastcom.social.pos.core.socket.listener.updatepolicy.UpdatePolicyRespListener;
import com.eastcom.social.pos.core.socket.message.updatepolicy.UpdatePolicyRespMessage;

public class MyUpdatePolicyRespListener implements UpdatePolicyRespListener {

	@Override
	public void handlerRespMessage(UpdatePolicyRespMessage message) {
		System.out.println("UpdatePolicy resp!");
	}

}
