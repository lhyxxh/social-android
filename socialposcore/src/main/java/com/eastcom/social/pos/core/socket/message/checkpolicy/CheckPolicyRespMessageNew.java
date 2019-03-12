package com.eastcom.social.pos.core.socket.message.checkpolicy;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class CheckPolicyRespMessageNew extends SoMessage {

	private int versionMain;

	private int versionSub;
	private int suffix_type;
	private int name_length;
	private String name;

	public CheckPolicyRespMessageNew(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		int position = 0;
		this.versionMain = ByteUtils.bytes2Int(body, position, 2);
		position += 2;
		this.versionSub = ByteUtils.bytes2Int(body, position, 2);
		position += 2;
		this.setSuffix_type(ByteUtils.bytes2Int(body, position, 1));
		position += 1;
		this.name_length = ByteUtils.bytes2Int(body, position, 2);
		position += 2;
		this.name = new String(body, position, name_length, CHARSET_GB);
		position += name_length;
	}

	public int getVersionMain() {
		return versionMain;
	}

	public void setVersionMain(int versionMain) {
		this.versionMain = versionMain;
	}

	public int getVersionSub() {
		return versionSub;
	}

	public void setVersionSub(int versionSub) {
		this.versionSub = versionSub;
	}

	public int getName_length() {
		return name_length;
	}

	public void setName_length(int name_length) {
		this.name_length = name_length;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSuffix_type() {
		return suffix_type;
	}

	public void setSuffix_type(int suffix_type) {
		this.suffix_type = suffix_type;
	}


}
