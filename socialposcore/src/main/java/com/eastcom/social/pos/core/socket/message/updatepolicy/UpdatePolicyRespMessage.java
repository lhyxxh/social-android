package com.eastcom.social.pos.core.socket.message.updatepolicy;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class UpdatePolicyRespMessage extends SoMessage {

	private int versionMain;

	private int versionSub;

	private int versionFlag;
	
	private int versionSize;
	
	private int dataTotalNo;

	private byte[] data;

	public UpdatePolicyRespMessage(SoMessage message) {
		super(message);

		this.versionMain = ByteUtils.bytes2Int(this.body, 0, 2);
		this.versionSub = ByteUtils.bytes2Int(this.body, 2, 2);
		this.versionFlag = ByteUtils.bytes2Int(this.body, 4, 4);
		this.versionSize = ByteUtils.bytes2Int(this.body, 8, 4);
		this.dataTotalNo = ByteUtils.bytes2Int(this.body, 12, 2);
		int dataLen = this.body.length - 14;
		this.data = new byte[dataLen];
		System.arraycopy(this.body, 14, this.data, 0, dataLen);
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

	public int getVersionSize() {
		return versionSize;
	}

	public void setVersionSize(int versionSize) {
		this.versionSize = versionSize;
	}

	public int getVersionFlag() {
		return versionFlag;
	}

	public void setVersionFlag(int versionFlag) {
		this.versionFlag = versionFlag;
	}

	public int getDataTotalNo() {
		return dataTotalNo;
	}

	public void setDataTotalNo(int dataTotalNo) {
		this.dataTotalNo = dataTotalNo;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
