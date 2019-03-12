package com.eastcom.social.pos.core.socket.message.socialinfo;

import com.eastcom.social.pos.core.socket.message.SoMessage;

public class SocailInfoRespMessage extends SoMessage {

	private String socialCardNo;


	public SocailInfoRespMessage(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		this.setSocialCardNo(new String(body,CHARSET));
	}


	public String getSocialCardNo() {
		return socialCardNo;
	}


	public void setSocialCardNo(String socialCardNo) {
		this.socialCardNo = socialCardNo;
	}



}
