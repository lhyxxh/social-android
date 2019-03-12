package com.eastcom.social.pos.core.socket.message;

public class SoCommand {

	public final static int NODATA = 0x0000FF00;

	public final static int 发送心跳包 = 0x0000FF01;
	public final static int 回应心跳包 = 0x0000FF81;
	
	public final static int 定点机构类型 = 0x0000FF02;
	public final static int 回应定点机构类型 = 0x0000FF82;

	public final static int 指令确认 = 0x0000FF40;
	public final static int 回应指令确认 = 0x0000FF40;

	public final static int 校验安卓固件版本 = 0x0000FF13;
	public final static int 回应校验安卓固件版本 = 0x0000FF93;

	public final static int 安卓固件更新 = 0x0000FF9F;
	public final static int 回应安卓固件更新 = 0x0000FF1F;

	public final static int 校验固件增量包版本 = 0x0000FF14;
	public final static int 回应校验固件增量包版本 = 0x0000FF94;

	public final static int 安卓增量包更新 = 0x0000FF9D;
	public final static int 回应安卓增量包更新 = 0x0000FF1D;

	public final static int 上传交易 = 0x0000FF27;
	public final static int 回应上传交易 = 0x0000FFA7;

	public final static int 上传交易明细 = 0x0000FF28;
	public final static int 回应上传交易明细 = 0x0000FFA8;

	public final static int 注册药品信息 = 0x0000FF34;
	public final static int 回应注册药品信息 = 0x0000FFB4;

	public final static int 注册保健品信息 = 0x0000FF35;
	public final static int 回应注册保健品信息 = 0x0000FFB5;

	public final static int 查询药品信息 = 0x0000FF44;
	public final static int 回应查询药品信息 = 0x0000FFC4;

	public final static int 查询保健品信息 = 0x0000FF45;
	public final static int 回应查询保健品信息 = 0x0000FFC5;


	public final static int 请求RFSAM默认有效时长 = 0x0000FF19;
	public final static int 回应请求RFSAM默认有效时长 = 0x0000FF99;

	public final static int 请求RFSAM卡名单 = 0x0000FF23;
	public final static int 回应请求RFSAM卡名单 = 0x0000FFA3;
	
	public final static int 上传告警 = 0x0000FF31;
	public final static int 回应上传告警 = 0x0000FFB1;
	
	public final static int 提交社保卡信息 = 0x0000FF52;
	public final static int 回应提交社保卡信息 = 0x0000FFD2;
	
	public final static int 请求修改标牌连接的服务器 = 0x0000FF18;
	public final static int 回应请求修改标牌连接的服务器 = 0x0000FF98;
	
	public final static int 请求交易数据是否被加密 = 0x0000FF1A;
	public final static int 回应请求交易数据是否被加密 = 0x0000FF9A;
	
	public final static int 请求心跳频率 = 0x0000FF12;
	public final static int 回应请求心跳频率= 0x0000FF92;
	
	public final static int 提交社保卡操作记录 = 0x0000FF53;
	public final static int 回应提交社保卡操作记录= 0x0000FFD3;
	
	public final static int 请求重启标牌 = 0x0000FF1F;
	public final static int 回应请求重启标牌 = 0x0000FFFF;
	
	public final static int 提交GPRS定位信息 = 0x0000FF29;
	public final static int 回应提交GPRS定位信息 = 0x0000FFA9;
	
	public final static int 请求校对黑名单版本 = 0x0000FF25;
	public final static int 回应请求校对黑名单版本 = 0x0000FFA5;
	
	public final static int 请求更新黑名单完整区 = 0x0000FF9E;
	public final static int 回应请求更新黑名单完整区 = 0x0000FF1E;
	
	public final static int 请求更新黑名单编辑区 = 0x0000FF26;
	public final static int 回应请求更新黑名单编辑区 = 0x0000FFA6;
	
	public final static int 查询社保卡状态 = 0x0000FF54;
	public final static int 回应查询社保卡状态 = 0x0000FFD4;
	
	public final static int 查询临沂社保卡状态 = 0x0000FF55;
	public final static int 回应查询临沂社保卡状态 = 0x0000FFD5;
	
	public final static int 查询珠海社保卡状态 = 0x0000FF59;
	public final static int 回应查询珠海社保卡状态 = 0x0000FFD9;
	
	public final static int 提交社保卡联系方式 = 0x0000FF51;
	public final static int 回应提交社保卡联系方式 = 0x0000FFD1;

	public final static int 提交固件版本指令 = 0x0000FF99;
	public final static int 回应提交固件版本指令 = 0x0000FF19;
	
	public final static int 获取系统短通知 = 0x0000FFCC;
	public final static int 回应获取系统短通知 = 0x0000FFDC;
	
	public final static int 获取系统时间 = 0x0000FF58;
	public final static int 回应获取系统时间 = 0x0000FFD8;
	
	public final static int 请求政策文件基本信息 = 0x0000FF56;
	public final static int 回应请求政策文件基本信息 = 0x0000FFD6;
	
	public final static int 请求政策文件数据包 = 0x0000FF57;
	public final static int 回应请求政策文件数据包 = 0x0000FFD7;
	
	public final static int 政策文件下载确认 = 0x0000FF5F;
	public final static int 回应政策文件下载确认 = 0x0000FFDF;

	public final static int 通用json字符串指令 = 0x0000FF61;
	public final static int 回应通用json字符串指令 = 0x0000FF61;

	public final static int 通用加密json字符串指令 = 0x0000FF62;
	public final static int 回应通用加密json字符串指令 = 0x0000FF62;

}
