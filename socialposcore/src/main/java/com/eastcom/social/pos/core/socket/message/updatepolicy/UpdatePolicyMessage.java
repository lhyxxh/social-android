package com.eastcom.social.pos.core.socket.message.updatepolicy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class UpdatePolicyMessage extends SoMessage {

	public UpdatePolicyMessage(int versionMain,int versionSub, int packetNo) {
		short bodyLength = (short) (4 + 4);

		ByteBuf bf = Unpooled.buffer(bodyLength);
		bf.writeBytes(ByteUtils.intToBytes2(versionMain));
		bf.writeBytes(ByteUtils.intToBytes2(versionSub));
		bf.writeBytes(ByteUtils.intToBytes(packetNo));

		this.command = SoCommand.请求政策文件数据包;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		bf.readBytes(body);

		super.computeChecksum();

	}

}
