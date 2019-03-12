package com.eastcom.social.pos.core.socket.message.smsinfo;

import com.eastcom.social.pos.core.socket.message.SoMessage;

public class SmsInfoRespMessage2 extends SoMessage {

	private String content;


	public SmsInfoRespMessage2(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		this.setContent(new String(body,CHARSET_GB));
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}





}
