package com.eastcom.social.pos.core.socket.message.alarm;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class AlarmMessage extends SoMessage {
	private String alarmType;
	private String code;

	public AlarmMessage(String alarmType,String code) {
		short bodyLength = (short)(1+code.getBytes(CHARSET).length);

		this.alarmType =  alarmType;
		this.code =  code;
		
		byte[] data = new byte[bodyLength];
		int position = 0;
		System.arraycopy(ByteUtils.hexStr2bytes(alarmType), 0, data, position, 1);
		position += 1;
		System.arraycopy(code.getBytes(CHARSET), 0, data, position, code.getBytes(CHARSET).length);
		
		this.command = SoCommand.上传告警;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();
	}

	public String getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}

	public String getTerminalCode() {
		return code;
	}

	public void setTerminalCode(String terminalCode) {
		this.code = terminalCode;
	}
	
	

}
