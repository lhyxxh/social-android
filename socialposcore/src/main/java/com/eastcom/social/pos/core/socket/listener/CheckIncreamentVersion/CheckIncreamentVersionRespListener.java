package com.eastcom.social.pos.core.socket.listener.CheckIncreamentVersion;

import com.eastcom.social.pos.core.socket.message.checkincreamentversion.CheckIncreamentVersionRespMessage;
import com.eastcom.social.pos.core.socket.message.checkversion.CheckVersionRespMessage;

public interface CheckIncreamentVersionRespListener {

    public void handlerRespMessage(CheckIncreamentVersionRespMessage message);
}
