package com.eastcom.social.pos.core.socket.listener.encrypt;

import com.eastcom.social.pos.core.socket.message.encrypt.EncryptRespMessage;

public interface EncryptRespListener {

	public void handlerRespMessage(EncryptRespMessage message);

}
