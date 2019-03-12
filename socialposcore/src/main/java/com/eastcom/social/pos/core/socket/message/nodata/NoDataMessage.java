package com.eastcom.social.pos.core.socket.message.nodata;

import com.eastcom.social.pos.core.socket.message.SoMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NoDataMessage extends SoMessage {
	private String nodata;

	public NoDataMessage(SoMessage message) {
		super(message);

		ByteBuf bf = Unpooled.copiedBuffer(this.body);
		this.nodata = bf.readCharSequence(this.getTotal(), CHARSET).toString();
	}

	@Override
	public String toString() {
		return super.toString() + "\r\n" + "body [nodata=" + nodata + "]";
	}

}
