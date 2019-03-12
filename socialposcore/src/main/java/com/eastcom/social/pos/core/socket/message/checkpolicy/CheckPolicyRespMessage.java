package com.eastcom.social.pos.core.socket.message.checkpolicy;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class CheckPolicyRespMessage extends SoMessage {

	private int versionMain;

	private int versionSub;

	private int name_length;

	private int suffix_length;

	private String name;
	private String suffix;

	public CheckPolicyRespMessage(SoMessage message) {
		super(message);

		byte[] body = message.getBody();
		int position = 0;
		this.versionMain = ByteUtils.bytes2Int(body, position, 2);
		position += 2;
		this.versionSub = ByteUtils.bytes2Int(body, position, 2);
		position += 2;
		this.name_length = ByteUtils.bytes2Int(body, position, 2);
		position += 2;
		this.suffix_length = ByteUtils.bytes2Int(body, position, 2);
		position += 2;
		this.name = new String(body, position, name_length, CHARSET_GB);
		position += name_length;
		this.suffix = new String(body, position, suffix_length, CHARSET);
		position += suffix_length;
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

	public int getSuffix_length() {
		return suffix_length;
	}

	public void setSuffix_length(int suffix_length) {
		this.suffix_length = suffix_length;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
