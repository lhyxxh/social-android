package com.eastcom.social.pos.core.socket.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

import com.eastcom.social.pos.core.utils.ByteUtils;

public class SoMessage {

	protected final static int SB_LENGTH = 8;
	protected final static Charset CHARSET = Charset.forName("ASCII");
	protected final static Charset CHARSET_GB = Charset.forName("GB2312");
	protected final static Charset CHARSET_UTF8 = Charset.forName("UTF-8");

	protected int command; // 指令
	protected short total; // 数据域长度
	protected byte[] body; // 数据域
	protected byte checksum; // 校验码
	protected byte end; // 结束符

	protected String signboard; // 8字节 电子标牌号

	public SoMessage() {

	}

	public SoMessage(SoMessage message) {
		this.command = message.getCommand();
		this.total = message.getTotal();
		this.body = message.getBody();
		this.checksum = message.getChecksum();
		this.end = message.getEnd();
	}

	/**
	 * 将电子标牌转为bytes
	 * 
	 * @return
	 */
	protected byte[] signboardToBytes() {
		return this.signboard.getBytes(CHARSET);
	}

	/**
	 * 将整包转为bytes
	 * 
	 * @return
	 */
	public byte[] convertToBytes() {
		int length = 4 + 2 + this.total + 2;

		ByteBuf bf = Unpooled.buffer(length);
		bf.writeInt(this.command);
		bf.writeShort(this.total);
		if (this.total > 0) {
			bf.writeBytes(this.body);
		}
		bf.writeByte(this.checksum);
		bf.writeByte(this.end);

		byte[] msg = new byte[length];
		bf.readBytes(msg);

		return msg;
	}

	/**
	 * 生成校验码
	 */
	public void computeChecksum() {
		byte[] b1 = ByteUtils.byteToBytes(ByteUtils.intToBytes(this.command)[3]);
		byte[] b2 = ByteUtils.shortToBytes(this.total);
		byte[] b3 = this.body;
		if (this.total > 0) {
			this.checksum = ByteUtils.getChecksum(b1, b2, b3);
		} else {
			this.checksum = ByteUtils.getChecksum(b1, b2);
		}
		this.end = 0x00;
	}

	/**
	 * 验证校验码
	 */
	public boolean validateChecksum() {
		byte[] b1 = ByteUtils.byteToBytes(ByteUtils.intToBytes(this.command)[3]);
		byte[] b2 = ByteUtils.shortToBytes(this.total);
		byte[] b3 = this.body;
		byte code;
		if (this.total > 0) {
			code = ByteUtils.getChecksum(b1, b2, b3);
		} else {
			code = ByteUtils.getChecksum(b1, b2);
		}
		return this.checksum == code;
	}

	public String toHexString() {
		return ByteUtils.toHexByteString(convertToBytes());
	}

	/**
	 * 获取指令对应的回应指令代码
	 */
	public int getRespCommand() {
		int result = SoCommand.NODATA;
		int cmd = this.getCommand();
		switch (cmd) {
		case SoCommand.发送心跳包:
			result = SoCommand.回应心跳包;
			break;
		case SoCommand.指令确认:
			result = SoCommand.回应指令确认;
			break;
		case SoCommand.上传交易:
			result = SoCommand.回应上传交易;
			break;
		case SoCommand.上传交易明细:
			result = SoCommand.回应上传交易明细;
			break;
		case SoCommand.注册药品信息:
			result = SoCommand.回应注册药品信息;
			break;
		case SoCommand.注册保健品信息:
			result = SoCommand.回应注册保健品信息;
			break;
		case SoCommand.查询药品信息:
			result = SoCommand.回应查询药品信息;
			break;
		case SoCommand.查询保健品信息:
			result = SoCommand.回应查询保健品信息;
			break;
		case SoCommand.校验安卓固件版本:
			result = SoCommand.回应校验安卓固件版本;
			break;
		case SoCommand.安卓固件更新:
			result = SoCommand.回应安卓固件更新;
			break;
		case SoCommand.校验固件增量包版本:
			result = SoCommand.回应校验固件增量包版本;
			break;
		case SoCommand.	安卓增量包更新:
			result = SoCommand.回应安卓增量包更新;
			break;
		case SoCommand.请求RFSAM卡名单:
			result = SoCommand.回应请求RFSAM卡名单;
			break;
		case SoCommand.请求RFSAM默认有效时长:
			result = SoCommand.回应请求RFSAM默认有效时长;
			break;
		case SoCommand.提交GPRS定位信息:
			result = SoCommand.回应提交GPRS定位信息;
			break;
		case SoCommand.上传告警:
			result = SoCommand.回应上传告警;
			break;
		case SoCommand.提交社保卡信息:
			result = SoCommand.回应提交社保卡信息;
			break;
		case SoCommand.请求修改标牌连接的服务器:
			result = SoCommand.回应请求修改标牌连接的服务器;
			break;
		case SoCommand.请求交易数据是否被加密:
			result = SoCommand.回应请求交易数据是否被加密;
			break;
		case SoCommand.请求心跳频率:
			result = SoCommand.回应请求心跳频率;
			break;
		case SoCommand.提交社保卡操作记录:
			result = SoCommand.回应提交社保卡操作记录;
			break;
		case SoCommand.请求重启标牌:
			result = SoCommand.回应请求重启标牌;
			break;
		case SoCommand.请求校对黑名单版本:
			result = SoCommand.回应请求校对黑名单版本;
			break;
		case SoCommand.请求更新黑名单完整区:
			result = SoCommand.回应请求更新黑名单完整区;
			break;
		case SoCommand.请求更新黑名单编辑区:
			result = SoCommand.回应请求更新黑名单编辑区;
			break;
		case SoCommand.查询社保卡状态:
			result = SoCommand.回应查询社保卡状态;
			break;
		case SoCommand.查询临沂社保卡状态:
			result = SoCommand.回应查询临沂社保卡状态;
			break;
		case SoCommand.查询珠海社保卡状态:
			result = SoCommand.回应查询珠海社保卡状态;
			break;
		case SoCommand.提交社保卡联系方式:
			result = SoCommand.回应提交社保卡联系方式;
			break;
		case SoCommand.获取系统短通知:
			result = SoCommand.回应获取系统短通知;
			break;
		case SoCommand.提交固件版本指令:
			result = SoCommand.回应提交固件版本指令;
			break;
		case SoCommand.获取系统时间:
			result = SoCommand.回应获取系统时间;
			break;
		case SoCommand.请求政策文件基本信息:
			result = SoCommand.回应请求政策文件基本信息;
			break;
		case SoCommand.请求政策文件数据包:
			result = SoCommand.回应请求政策文件数据包;
			break;
		case SoCommand.政策文件下载确认:
			result = SoCommand.回应政策文件下载确认;
			break;
		case SoCommand.定点机构类型:
			result = SoCommand.回应定点机构类型;
			break;
		}

		return result;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public short getTotal() {
		return total;
	}

	public void setTotal(short total) {
		this.total = total;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public byte getChecksum() {
		return checksum;
	}

	public void setChecksum(byte checksum) {
		this.checksum = checksum;
	}

	public byte getEnd() {
		return end;
	}

	public void setEnd(byte end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "head [command=" + ByteUtils.toHexString(ByteUtils.intToBytes(this.command)) + ",total=" + total + "]"
				+ "\r\n" + "foot [checksum=" + ByteUtils.toHexString(checksum) + ",end=" + end + "]";
	}

}
