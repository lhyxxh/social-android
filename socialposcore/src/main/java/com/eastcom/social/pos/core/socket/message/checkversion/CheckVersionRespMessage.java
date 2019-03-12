package com.eastcom.social.pos.core.socket.message.checkversion;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class CheckVersionRespMessage extends SoMessage {

	private int versionMainUpdate;

	private int versionSubUpdate;

	private int allowUpdate;

	public CheckVersionRespMessage(SoMessage message) {
		super(message);

		this.versionMainUpdate = ByteUtils.bytes2Int(this.body, 0, 2);
		this.versionSubUpdate = ByteUtils.bytes2Int(this.body, 2, 2);
		this.allowUpdate = ByteUtils.bytes2Int(this.body, 4, 1);
	}


	public int getVersionMainUpdate() {
		return versionMainUpdate;
	}


	public void setVersionMainUpdate(int versionMainUpdate) {
		this.versionMainUpdate = versionMainUpdate;
	}


	public int getVersionSubUpdate() {
		return versionSubUpdate;
	}


	public void setVersionSubUpdate(int versionSubUpdate) {
		this.versionSubUpdate = versionSubUpdate;
	}


	public int getAllowUpdate() {
		return allowUpdate;
	}

	public void setAllowUpdate(int allowUpdate) {
		this.allowUpdate = allowUpdate;
	}


}
