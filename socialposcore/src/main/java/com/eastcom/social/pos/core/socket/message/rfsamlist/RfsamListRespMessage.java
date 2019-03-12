package com.eastcom.social.pos.core.socket.message.rfsamlist;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class RfsamListRespMessage extends SoMessage {

	private String rfsamNo;

	private int rfsamStatus;

	private int rfsamLeftInterval;

	private String psamNo;

	public RfsamListRespMessage(SoMessage message) {
		super(message);
		try {
			ByteBuf buf = Unpooled.copiedBuffer(this.body);
			setRfsamNo(buf.readCharSequence(8, CHARSET).toString());
			setRfsamStatus(buf.readByte());
			setRfsamLeftInterval(buf.readByte());
			byte[] psamNoB = new byte[6];
			buf.readBytes(6).readBytes(psamNoB);
			setPsamNo(ByteUtils.bcd2Str(psamNoB));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getRfsamNo() {
		return rfsamNo;
	}

	public void setRfsamNo(String rfsamNo) {
		this.rfsamNo = rfsamNo;
	}

	public int getRfsamStatus() {
		return rfsamStatus;
	}

	public void setRfsamStatus(int rfsamStatus) {
		this.rfsamStatus = rfsamStatus;
	}

	public int getRfsamLeftInterval() {
		return rfsamLeftInterval;
	}

	public void setRfsamLeftInterval(int rfsamLeftInterval) {
		this.rfsamLeftInterval = rfsamLeftInterval;
	}

	public String getPsamNo() {
		return psamNo;
	}

	public void setPsamNo(String psamNo) {
		this.psamNo = psamNo;
	}

}
