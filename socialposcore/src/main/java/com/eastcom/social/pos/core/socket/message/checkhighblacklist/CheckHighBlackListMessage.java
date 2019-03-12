package com.eastcom.social.pos.core.socket.message.checkhighblacklist;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class CheckHighBlackListMessage extends SoMessage {

	private int versionMain;

	private int versionSub;


	public CheckHighBlackListMessage(int versionMain, int versionSub) {
		short bodyLength = 4;

		this.versionMain = versionMain;
		this.versionSub = versionSub;

		ByteBuf bf = Unpooled.buffer(bodyLength);
		bf.writeBytes(ByteUtils.intToBytes2(versionMain));
		bf.writeBytes(ByteUtils.intToBytes2(versionSub));

		this.command = SoCommand.请求校对黑名单版本;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		bf.readBytes(body);

		super.computeChecksum();
	}

	public CheckHighBlackListMessage(SoMessage message) {
		super(message);

		this.versionMain = ByteUtils.bytes2Int(this.body, 0, 2);
		this.versionSub = ByteUtils.bytes2Int(this.body, 2, 2);
	}

	@Override
	public String toString() {
		return "UpdateBlackListMessage [versionMain=" + versionMain
				+ ", versionSub=" + versionSub + "]";
	}


}
