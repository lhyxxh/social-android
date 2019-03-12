package com.eastcom.social.pos.core.socket.client.mylistener.querymedicine;

import com.eastcom.social.pos.core.socket.listener.querymedicine.QueryMedicineRespListener;
import com.eastcom.social.pos.core.socket.message.querymedicine.QueryMedicineRespMessage;

public class MyQueryMedicineRespListener implements QueryMedicineRespListener {

	@Override
	public void handlerRespMessage(QueryMedicineRespMessage message) {
		System.out.println("querymedicine resp!");
	}

}
