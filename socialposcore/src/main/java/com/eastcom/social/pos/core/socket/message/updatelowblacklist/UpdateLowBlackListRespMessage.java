package com.eastcom.social.pos.core.socket.message.updatelowblacklist;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class UpdateLowBlackListRespMessage extends SoMessage {

	private int versionMain;
	private int versionSub;

	private int versionFlag;

	private int dataNo;

	private int differenceTotal;
	private int dataTotal;

	private byte[] data;

	public UpdateLowBlackListRespMessage(SoMessage message) {
		super(message);

		this.versionMain = ByteUtils.bytes2Int(this.body, 0, 2);
		this.versionSub = ByteUtils.bytes2Int(this.body, 2, 2);
		this.versionFlag = ByteUtils.bytes2Int(this.body, 4, 1);
		this.dataNo = ByteUtils.bytes2Int(this.body, 5, 1);
		this.differenceTotal = ByteUtils.bytes2Int(this.body, 6, 4);
		this.dataTotal = ByteUtils.bytes2Int(this.body, 10, 1);
		int dataLen = this.body.length - 11;
		this.data = new byte[dataLen];
		System.arraycopy(this.body, 11, this.data, 0, dataLen);
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public int getVersionSub() {
		return versionSub;
	}

	public void setVersionSub(int versionSub) {
		this.versionSub = versionSub;
	}

	public int getVersionMain() {
		return versionMain;
	}

	public void setVersionMain(int versionMain) {
		this.versionMain = versionMain;
	}

	public int getVersionFlag() {
		return versionFlag;
	}

	public void setVersionFlag(int versionFlag) {
		this.versionFlag = versionFlag;
	}

	public int getDataNo() {
		return dataNo;
	}

	public void setDataNo(int dataNo) {
		this.dataNo = dataNo;
	}

	public int getDifferenceTotal() {
		return differenceTotal;
	}

	public void setDifferenceTotal(int differenceTotal) {
		this.differenceTotal = differenceTotal;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public int getDataTotal() {
		return dataTotal;
	}

	public void setDataTotal(int dataTotal) {
		this.dataTotal = dataTotal;
	}

}
