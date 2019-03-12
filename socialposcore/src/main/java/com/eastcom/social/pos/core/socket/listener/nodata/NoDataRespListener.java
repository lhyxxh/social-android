package com.eastcom.social.pos.core.socket.listener.nodata;

import com.eastcom.social.pos.core.socket.message.nodata.NoDataMessage;

public interface NoDataRespListener {

	public void handlerRespMessage(NoDataMessage message);

}
