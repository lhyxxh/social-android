package com.eastcom.social.pos.core.socket.message.socketurl;

import com.eastcom.social.pos.core.socket.message.SoMessage;

public class SocketUrlRespMessage extends SoMessage {

	private String urlString;

	public SocketUrlRespMessage(SoMessage message) {
		super(message);
		byte[] body = message.getBody();
		setUrlString(new String(body,CHARSET));
	}

	public String getUrlString() {
		return urlString;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

}
