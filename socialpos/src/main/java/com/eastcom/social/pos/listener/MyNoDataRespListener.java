package com.eastcom.social.pos.listener;

import com.eastcom.social.pos.core.socket.listener.nodata.NoDataRespListener;
import com.eastcom.social.pos.core.socket.message.nodata.NoDataMessage;

public class MyNoDataRespListener implements NoDataRespListener {
	

	@Override
	public void handlerRespMessage(NoDataMessage message) {
		System.out.println("nodata");
	}

}
