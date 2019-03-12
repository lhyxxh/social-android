package com.eastcom.social.pos.core.socket.message;

public class SoFollowCommad {

	public final static byte 没有后续动作 = 0x00;

	public final static byte 更新RFSAM名单 = 0x01;
	public final static byte 更新心跳频率 = 0x02;
	public final static byte 更新服务器地址 = 0x18;
	public final static byte 更新RFSAM卡默认有效时间 = 0x19;
	public final static byte 更新交易是否加密配置 = 0x1A;
	public final static byte 校验黑名单版本 = 0x26;
	public final static byte 请求校对政策文件版本 = 0x28;
	public final static byte 请求更新固件 = 0x14;
	public final static byte 上传监控图片 = 0x30;
	public final static byte 请求定点机构类型 = 0x31;

	public final static byte 重启标牌 = 0x1F;

}
