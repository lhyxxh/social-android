package com.eastcom.social.pos.core.socket.message.updatehighblacklist;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class UpdateHighBlackListMessage extends SoMessage {

	private int versionMain;

	private int dataNo;


	public UpdateHighBlackListMessage(int versionMain, int dataNo) {
		short bodyLength = 4;

		this.versionMain = versionMain;
		this.dataNo = dataNo;

		ByteBuf bf = Unpooled.buffer(bodyLength);
		bf.writeBytes(ByteUtils.intToBytes2(versionMain));
		bf.writeBytes(ByteUtils.intToBytes2(dataNo));

		this.command = SoCommand.请求更新黑名单完整区;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		bf.readBytes(body);

		super.computeChecksum();
	}

	public UpdateHighBlackListMessage(SoMessage message) {
		super(message);

		this.versionMain = ByteUtils.bytes2Int(this.body, 0, 2);
		this.dataNo = ByteUtils.bytes2Int(this.body, 2, 2);
	}

	@Override
	public String toString() {
		return "UpdateBlackListMessage [versionMain=" + versionMain
				+ ", dataNo=" + dataNo + "]";
	}


}
