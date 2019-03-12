package com.eastcom.social.pos.core.socket.message.checksocialstatus;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.eastcom.social.pos.core.socket.message.SoMessage;

public class CheckSocialStatusRespMessage extends SoMessage {

	private String socialCardNo;
	private int status;

	public CheckSocialStatusRespMessage(SoMessage message) {
		super(message);

		try {
			ByteBuf buf = Unpooled.copiedBuffer(this.body);
			setSocialCardNo(buf.readCharSequence(9, CHARSET).toString());
			setStatus(buf.readByte());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSocialCardNo() {
		return socialCardNo;
	}

	public void setSocialCardNo(String socialCardNo) {
		this.socialCardNo = socialCardNo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}




}
