package com.eastcom.social.pos.core.socket.message.checkhighblacklist;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class CheckHighBlackListRespMessage extends SoMessage {

	private int versionMain;

	private int versionSub;




	public CheckHighBlackListRespMessage(SoMessage message) {
		super(message);

		this.versionMain = ByteUtils.bytes2Int(this.body, 0, 2);
		this.versionSub = ByteUtils.bytes2Int(this.body, 2, 2);
	}

	@Override
	public String toString() {
		return super.toString();
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



}
