package com.eastcom.social.pos.core.socket.client.mylistener.CheckIncrementVersion;

import com.eastcom.social.pos.core.socket.listener.CheckIncreamentVersion.CheckIncreamentVersionRespListener;
import com.eastcom.social.pos.core.socket.message.checkincreamentversion.CheckIncreamentVersionRespMessage;

public class MyCheckIncrementVersionRespListener implements CheckIncreamentVersionRespListener {
    @Override
    public void handlerRespMessage(CheckIncreamentVersionRespMessage message) {
        System.out.println("CheckIncreamentVersionRespMessage resp!");
    }
}
