package com.eastcom.social.pos.core.socket.message.confirm;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class ConfirmMssage extends SoMessage {

	private int cmdCode;

	private int cmdStatus;

	public ConfirmMssage(int cmdCode, int cmdStatus) {
		short bodyLength = 2;

		this.cmdCode = cmdCode;
		this.cmdStatus = cmdStatus;

		byte[] data = new byte[bodyLength];
		data[0] = (byte) cmdCode;
		data[1] =  (byte) cmdStatus;
		
		this.command = SoCommand.指令确认;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();
	}

	public int getCmdCode() {
		return cmdCode;
	}

	public void setCmdCode(int cmdCode) {
		this.cmdCode = cmdCode;
	}

	public int getCmdStatus() {
		return cmdStatus;
	}

	public void setCmdStatus(int cmdStatus) {
		this.cmdStatus = cmdStatus;
	}

}
