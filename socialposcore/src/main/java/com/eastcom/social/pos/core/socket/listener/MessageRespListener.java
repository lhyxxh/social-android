package com.eastcom.social.pos.core.socket.listener;

import java.util.EventListener;

import com.eastcom.social.pos.core.socket.message.SoMessage;

/**
 * 消息响应监听器
 */
public interface MessageRespListener extends EventListener {

	public void handlerRespMessage(SoMessage message);

}
