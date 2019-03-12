package com.eastcom.social.pos.core.socket.message.heartbeat;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class HeartbeatRespMessage extends SoMessage {

	public byte followCommand; // 1字节 后续指令

	public HeartbeatRespMessage(String signboard, byte followCommand) {
		short bodyLength = SB_LENGTH + 1;

		this.signboard = signboard;
		this.followCommand = followCommand;

		ByteBuf bf = Unpooled.buffer(bodyLength);
		bf.writeBytes(signboardToBytes());
		bf.writeByte(this.followCommand);

		this.command = SoCommand.回应心跳包;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		bf.readBytes(body);

		super.computeChecksum();
	}

	public HeartbeatRespMessage(SoMessage message) {
		super(message);

		ByteBuf bf = Unpooled.copiedBuffer(this.body);
		this.signboard = bf.readCharSequence(SB_LENGTH, CHARSET).toString();
		this.followCommand = bf.readByte();
	}

	public byte getFollowCommand() {
		return followCommand;
	}

	public void setFollowCommand(byte followCommand) {
		this.followCommand = followCommand;
	}

	@Override
	public String toString() {
		return super.toString() + "\r\n" + "body [signboard=" + signboard + ",followCommand="
				+ ByteUtils.toHexString(followCommand) + "]";
	}
}
