package com.eastcom.social.pos.core.socket.listener.updatelowblacklist;

import com.eastcom.social.pos.core.socket.message.updatelowblacklist.UpdateLowBlackListRespMessage;



public interface UpdateLowBlackListRespListener {

	public void handlerRespMessage(UpdateLowBlackListRespMessage message);

}
