package com.eastcom.social.pos.core.socket.client.mylistener.healthproduct;

import com.eastcom.social.pos.core.socket.listener.healthproduct.HealthProductRespListener;
import com.eastcom.social.pos.core.socket.message.healthproduct.HealthProductRespMessage;

public class MyHealthProductRespListener implements HealthProductRespListener {

	@Override
	public void handlerRespMessage(HealthProductRespMessage message) {
		System.out.println("healthproduct resp!");
	}

}
