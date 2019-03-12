package com.eastcom.social.pos.core.socket.message.setheartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class SetHeartBeatRespMessage extends SoMessage {

	private int heartbeat;
	private int rfsamLeftInterval;
	private int allow_offline;
	private int move_offline;
	private String allow_offline_time;

	public SetHeartBeatRespMessage(SoMessage message) {
		super(message);
		try {
			ByteBuf buf = Unpooled.copiedBuffer(this.body);
			setHeartbeat(buf.readByte());
			setRfsamLeftInterval(buf.readByte());
			setAllow_offline(buf.readByte());
			setMove_offline(buf.readByte());
			byte[] allow_offline_time = new byte[4];
			buf.readBytes(4).readBytes(allow_offline_time);
			setAllow_offline_time(ByteUtils.bcd2Str(allow_offline_time));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(int heartbeat) {
		this.heartbeat = heartbeat;
	}

	public int getRfsamLeftInterval() {
		return rfsamLeftInterval;
	}

	public void setRfsamLeftInterval(int rfsamLeftInterval) {
		this.rfsamLeftInterval = rfsamLeftInterval;
	}

	public int getAllow_offline() {
		return allow_offline;
	}

	public void setAllow_offline(int allow_offline) {
		this.allow_offline = allow_offline;
	}

	public int getMove_offline() {
		return move_offline;
	}

	public void setMove_offline(int move_offline) {
		this.move_offline = move_offline;
	}

	public String getAllow_offline_time() {
		return allow_offline_time;
	}

	public void setAllow_offline_time(String allow_offline_time) {
		this.allow_offline_time = allow_offline_time;
	}

	
	

}
