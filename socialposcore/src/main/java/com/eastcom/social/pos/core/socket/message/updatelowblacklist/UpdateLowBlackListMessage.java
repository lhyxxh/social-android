package com.eastcom.social.pos.core.socket.message.updatelowblacklist;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class UpdateLowBlackListMessage extends SoMessage {

	private int versionMain;
	private int versionSub;
	private int dataNo;

	public UpdateLowBlackListMessage(int versionMain, int versionSub, int dataNo) {
		short bodyLength = 5;

		this.versionMain = versionMain;
		this.versionSub = versionSub;
		this.dataNo = dataNo;

		ByteBuf bf = Unpooled.buffer(bodyLength);
		bf.writeBytes(ByteUtils.intToBytes2(versionMain));
		bf.writeBytes(ByteUtils.intToBytes2(versionSub));
		bf.writeBytes(ByteUtils.byteToBytes((byte) dataNo));

		this.command = SoCommand.请求更新黑名单编辑区;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		bf.readBytes(body);

		super.computeChecksum();
	}

	public UpdateLowBlackListMessage(SoMessage message) {
		super(message);

		this.versionMain = ByteUtils.bytes2Int(this.body, 0, 2);
		this.versionSub = ByteUtils.bytes2Int(this.body, 2, 2);
		this.dataNo = ByteUtils.bytes2Int(this.body, 4, 1);
	}

	@Override
	public String toString() {
		return "UpdateLowBlackListMessage [versionMain=" + versionMain
				+ ", versionSub=" + versionSub + ", dataNo=" + dataNo + "]";
	}


}
