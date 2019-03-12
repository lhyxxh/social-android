package com.eastcom.social.pos.core.socket.message;

public class SoSocialCardType {

	public static String getSocialCardType(int type) {
		String msg = "";
		switch (type) {
		case 0:
			msg = "该卡已放号";
			break;
		case 1:
			msg = "正常";
			break;
		case 2:
			msg = "该卡已挂失";
			break;
		case 3:
			msg = "该卡已注销";
			break;
		case 4:
			msg = "该卡已注销";
			break;
		case 5:
			msg = "该卡已列入黑名单";
			break;
		case 6:
			msg = "该卡未启用";
			break;
		case 7:
			msg = "该卡暂时封存";
			break;
		case 10:
			msg = "该卡未启用";
			break;
		case 11:
			msg = "该卡已临时挂失";
			break;
		case 100:
			msg = "查询社保卡有效性超时";
			break;
		case 101:
			msg = "查询社保卡有效性异常";
			break;
		case 200:
			msg = "---";
			break;
		default:
			msg = "未查询到卡号记录";
			break;
		}
		return msg;
	}
	public static String getSdLySocialCardType(int type) {
		String msg = "";
		switch (type) {
		case 0:
			msg = "不存在对应社保卡号";
			break;
		case 1:
			msg = "正常";
			break;
		case 2:
			msg = "状态正常，密码错误";
			break;
		case 3:
			msg = "状态异常";
			break;
		case 4:
			msg = "身份异常";
			break;
		default:
			msg = "未查询到卡号记录";
			break;
		}
		return msg;
	}
}
