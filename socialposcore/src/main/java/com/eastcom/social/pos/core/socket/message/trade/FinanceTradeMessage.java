package com.eastcom.social.pos.core.socket.message.trade;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

/**
 * 金融交易只有明文的
 */
@SuppressLint("SimpleDateFormat")
public class FinanceTradeMessage extends SoMessage {

	/**
	 * 82字节金融账户交易和医保个账交易指令格式一致
	 */
	public FinanceTradeMessage(String tradeType, String tradeState,
			String isEncrypt, int preNo, String bankCard, String socialCardNo,
			String refNo, Date tradeTime, String tradeSn, int tradeMoney,
			String psamNo, String terminalCode, String rfsamNo,
			int tradeRandom, int mac, int sendCount, int tradeNo) {
		short bodyLength = 97;

		byte[] data = new byte[bodyLength];
		int position = 0;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeType), 0, data, position,
				1);
		position += 1;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeState), 0, data, position,
				2);
		position += 2;
		System.arraycopy(ByteUtils.hexStr2bytes(isEncrypt), 0, data, position,
				1);
		position += 1;
		System.arraycopy(ByteUtils.intToBytes(preNo), 0, data, position, 4);
		position += 4;
		position += 2;
		System.arraycopy(ByteUtils.hexStr2bytes(bankCard), 0, data, position,
				10);
		position += 10;
		// 社保卡填充
		data[position] = (byte) 0xad;
		position += 5;
		position += 1;
		System.arraycopy(ByteUtils.hexStr2bytes(refNo), 0, data, position, 6);
		position += 6;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		System.arraycopy(ByteUtils.hexStr2bytes(sdf.format(tradeTime)), 0,
				data, position, 7);
		position += 7;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeSn), 0, data, position, 3);
		position += 3;
		System.arraycopy(ByteUtils.intToBytes(tradeMoney), 0, data, position, 4);
		position += 4;
		// 修改psam卡号，截取前6位，后6位
		StringBuffer sb = new StringBuffer();
		sb.append(psamNo.substring(0, 6));
		sb.append(psamNo.substring(psamNo.length() - 6, psamNo.length()));
		System.arraycopy(ByteUtils.hexStr2bytes(sb.toString()), 0, data,
				position, 6);
		position += 6;
		System.arraycopy(ByteUtils.hexStr2bytes(terminalCode), 0, data,
				position, 4);
		position += 4;
		System.arraycopy(rfsamNo.getBytes(CHARSET), 0, data, position, 8);
		position += 8;
		System.arraycopy(socialCardNo.getBytes(CHARSET), 0, data, position, 9);
		position += 9;
		position += 1;
		System.arraycopy(ByteUtils.intToBytes(tradeRandom), 0, data, position,
				4);
		position += 4;
		System.arraycopy(ByteUtils.intToBytes(mac), 0, data, position, 4);
		position += 4;
		System.arraycopy(rfsamNo.getBytes(CHARSET), 0, data, position, 8);
		position += 8;
		System.arraycopy(ByteUtils.intToBytes(tradeRandom), 0, data, position,
				4);
		position += 4;
		data[position] = (byte) sendCount;
		position += 1;
		System.arraycopy(ByteUtils.intToBytes2(tradeNo), 0, data, position, 2);
		position += 2;

		this.command = SoCommand.上传交易;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();
	}

	public FinanceTradeMessage(String tradeType, String tradeState,
			String isEncrypt, int preNo, String bankCard, String socialCardNo,
			String refNo, Date tradeTime, String tradeSn, int tradeMoney,
			String psamNo, String terminalCode, String rfsamNo,
			int tradeRandom, int mac, int sendCount, int tradeNo, String payType) {
		short bodyLength = 97;

		byte[] data = new byte[bodyLength];
		int position = 0;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeType), 0, data, position,
				1);
		position += 1;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeState), 0, data, position,
				2);
		position += 2;
		System.arraycopy(ByteUtils.hexStr2bytes(isEncrypt), 0, data, position,
				1);
		position += 1;
		System.arraycopy(ByteUtils.intToBytes(preNo), 0, data, position, 4);
		position += 4;
		System.arraycopy(ByteUtils.hexStr2bytes(payType), 0, data, position, 1);
		position += 1;
		position += 1;
		System.arraycopy(ByteUtils.hexStr2bytes(bankCard), 0, data, position,
				10);
		position += 10;
		// 社保卡填充
		data[position] = (byte) 0xfc;
		position += 1;
		System.arraycopy(socialCardNo.substring(0, 1).getBytes(CHARSET), 0,
				data, position, 1);
		position += 1;
		System.arraycopy(ByteUtils.intToBytes3(Integer.valueOf(socialCardNo
				.substring(1, 8))), 0, data, position, 3);
		position += 3;
		System.arraycopy(socialCardNo.substring(8, 9).getBytes(CHARSET), 0,
				data, position, 1);
		position += 1;
		System.arraycopy(ByteUtils.hexStr2bytes(refNo), 0, data, position, 6);
		position += 6;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		System.arraycopy(ByteUtils.hexStr2bytes(sdf.format(tradeTime)), 0,
				data, position, 7);
		position += 7;
		System.arraycopy(ByteUtils.hexStr2bytes(tradeSn), 0, data, position, 3);
		position += 3;
		System.arraycopy(ByteUtils.intToBytes(tradeMoney), 0, data, position, 4);
		position += 4;
		// 修改psam卡号，截取前6位，后6位
		StringBuffer sb = new StringBuffer();
		sb.append(psamNo.substring(0, 6));
		sb.append(psamNo.substring(psamNo.length() - 6, psamNo.length()));
		System.arraycopy(ByteUtils.hexStr2bytes(sb.toString()), 0, data,
				position, 6);
		position += 6;
		System.arraycopy(ByteUtils.hexStr2bytes(terminalCode), 0, data,
				position, 4);
		position += 4;
		System.arraycopy(rfsamNo.getBytes(CHARSET), 0, data, position, 8);
		position += 8;
		position += 10;
		System.arraycopy(ByteUtils.intToBytes(tradeRandom), 0, data, position,
				4);
		position += 4;
		System.arraycopy(ByteUtils.intToBytes(mac), 0, data, position, 4);
		position += 4;
		System.arraycopy(rfsamNo.getBytes(CHARSET), 0, data, position, 8);
		position += 8;
		System.arraycopy(ByteUtils.intToBytes(tradeRandom), 0, data, position,
				4);
		position += 4;
		data[position] = (byte) sendCount;
		position += 1;
		System.arraycopy(ByteUtils.intToBytes2(tradeNo), 0, data, position, 2);
		position += 2;

		this.command = SoCommand.上传交易;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();
	}
}
