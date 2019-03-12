package com.eastcom.social.pos.core.socket.message.telinfo;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class TelInfoMessage extends SoMessage {
	
	private String socialCardNo;
	private String tel;
	

	public TelInfoMessage(String socialCardNo, String tel) {
		short bodyLength = (short) (15);

		this.socialCardNo = socialCardNo;
		this.tel = tel;

		byte[] data = new byte[bodyLength];

		int position = 0;
		System.arraycopy(socialCardNo.getBytes(CHARSET), 0, data, position, 9);
		position += 9;
		System.arraycopy(ByteUtils.hexStr2bytes(tel), 0, data, position, 6);
		position += 6;

		this.command = SoCommand.提交社保卡联系方式;
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


	public String getTel() {
		return tel;
	}


	public void setTel(String tel) {
		this.tel = tel;
	}


    
}
