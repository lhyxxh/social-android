package com.eastcom.social.pos.core.socket.message.updatesoft;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class UpdateSoftMessage extends SoMessage {

	public UpdateSoftMessage(String versionMain, String versionSub,
			String packetNo) {
		short bodyLength = (short) (2 + 2 + 4);

		try {
			byte[] data = new byte[bodyLength];
			int position = 0;
			byte[] hexStr2bytes = ByteUtils.hexStr2bytes(versionMain);
			System.arraycopy(versionMain.getBytes(CHARSET_GB), 0, data,
					position, 2);
			position += 2;
			System.arraycopy(versionSub.getBytes(), 0, data, position, 2);
			position += 2;
			System.arraycopy(packetNo.getBytes(), 0, data, position, 4);
			position += 4;

			this.command = SoCommand.安卓固件更新;
			this.total = bodyLength;
			this.body = new byte[bodyLength];
			System.arraycopy(data, 0, this.body, 0, bodyLength);

			super.computeChecksum();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public UpdateSoftMessage(int versionMain, int versionSub,
			int packetNo) {
		short bodyLength = (short) (2 + 2 + 4);
		
		try {
			byte[] data = new byte[bodyLength];
//			int position = 0;
//			System.arraycopy(ByteUtils.intToBytes2(versionMain), 0, data,
//					position, 2);
//			position += 2;
//			System.arraycopy(ByteUtils.intToBytes2(versionSub), 0, data, position, 2);
//			position += 2;
//			System.arraycopy(ByteUtils.intToBytes(packetNo), 0, data, position, 4);
//			position += 4;
			
			
			ByteBuf bf = Unpooled.buffer(bodyLength);

			bf.writeShort(Integer.valueOf(versionMain));
			bf.writeShort(Integer.valueOf(versionSub));
			bf.writeInt(Integer.valueOf(packetNo));

			bf.readBytes(data);
			
			this.command = SoCommand.安卓固件更新;
			this.total = bodyLength;
			this.body = new byte[bodyLength];
			System.arraycopy(data, 0, this.body, 0, bodyLength);
			
			super.computeChecksum();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
