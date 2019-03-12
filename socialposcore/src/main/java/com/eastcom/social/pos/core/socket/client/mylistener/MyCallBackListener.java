package com.eastcom.social.pos.core.socket.client.mylistener;

import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class MyCallBackListener implements ActivityCallBackListener {

	@Override
	public void callBack(SoMessage message) {
		System.out.println("call back!");
	}

	@Override
	public void doTimeOut() {
		System.out.println("time out!");
	}

}
