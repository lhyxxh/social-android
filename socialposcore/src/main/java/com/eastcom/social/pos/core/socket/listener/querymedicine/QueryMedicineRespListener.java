package com.eastcom.social.pos.core.socket.listener.querymedicine;

import com.eastcom.social.pos.core.socket.message.querymedicine.QueryMedicineRespMessage;

public interface QueryMedicineRespListener {

	public void handlerRespMessage(QueryMedicineRespMessage message);

}
