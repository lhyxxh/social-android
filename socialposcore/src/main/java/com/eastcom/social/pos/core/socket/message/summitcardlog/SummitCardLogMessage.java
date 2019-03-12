package com.eastcom.social.pos.core.socket.message.summitcardlog;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class SummitCardLogMessage extends SoMessage {
	private String socialCardNo;
	private int desc;
	
	public SummitCardLogMessage(String socialCardNo, int desc) {
		short bodyLength = (short) (9  + 1 );
		
		this.socialCardNo = socialCardNo;
		this.desc = desc;
		
		byte[] data = new byte[bodyLength];
		
		int position = 0;
		System.arraycopy(socialCardNo.getBytes(CHARSET), 0, data, position, 9);
		position += 9;
		data[position] = (byte) desc;
		
		this.command = SoCommand.提交社保卡操作记录;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);
		
		super.computeChecksum();
	}

	public String getSocialCardNo() {
		return socialCardNo;
	}

	public void setSocialCardNo(String socialCardNo) {
		this.socialCardNo = socialCardNo;
	}

	public int getDesc() {
		return desc;
	}

	public void setDesc(int desc) {
		this.desc = desc;
	}

}
