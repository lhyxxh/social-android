package com.eastcom.social.pos.core.socket.listener;

import java.util.EventListener;

import com.eastcom.social.pos.core.socket.message.SoMessage;

/**
 * 消息响应的回调事件，暂配置于全局
 */
public interface ActivityCallBackListener extends EventListener {

	public void callBack(SoMessage message);

	public void doTimeOut();

}
