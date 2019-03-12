package com.eastcom.social.pos.core.socket.message.updatehighblacklist;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class UpdateHighBlackListRespMessage extends SoMessage {

	private int versionMain;
	
	private int dataNo;
	
	private int versionSize;

	private int dataTotalNo;

	private byte[] data;

	public UpdateHighBlackListRespMessage(SoMessage message) {
		super(message);

		this.versionMain = ByteUtils.bytes2Int(this.body, 0, 2);
		this.dataNo = ByteUtils.bytes2Int(this.body, 2, 2);
		this.versionSize = ByteUtils.bytes2Int(this.body, 4, 2);
		this.dataTotalNo = ByteUtils.bytes2Int(this.body, 6, 2);
		int dataLen = this.body.length - 8;
		this.data = new byte[dataLen];
		System.arraycopy(this.body, 8, this.data, 0, dataLen);
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


	public int getVersionSize() {
		return versionSize;
	}

	public void setVersionSize(int versionSize) {
		this.versionSize = versionSize;
	}

	public int getDataNo() {
		return dataNo;
	}

	public void setDataNo(int dataNo) {
		this.dataNo = dataNo;
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
