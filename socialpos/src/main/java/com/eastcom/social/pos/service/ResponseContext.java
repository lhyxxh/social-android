package com.eastcom.social.pos.service;

public class ResponseContext {
	
	//服务器端返回的状态提示  
	public static final String HTTP_0="其他错误"; 
	public static final String HTTP_1="";
	public static final String HTTP_401="未授权";
	public static final String HTTP_403="服务器拒绝请求";
	public static final String HTTP_404="404错误,请求链接无效";
	public static final String HTTP_414="414错误,请求URI过长";
	public static final String HTTP_500="网络500错误,服务器端程序出错";
	public static final String HTTP_900="网络传输协议出错";
	public static final String HTTP_901="连接超时";
	public static final String HTTP_902="网络中断";
	public static final String HTTP_903="网络数据流传输出错";
	public static final String HTTP_UNKONW="未知的错误";
	
	//数据结果
	private String result;
	//友好化消息提示
	private String tip;
	//消息
	private String message;
	//网络响应代码
	private int statusCode=1;
	
	public String getStatusCodeMessage()
	{
		String message=HTTP_UNKONW;
		if(statusCode>=200&&statusCode<=206)
		{
			message=HTTP_1;
		}
		else {
			switch(statusCode){
				case 401:message=HTTP_401;break;
				case 403:message=HTTP_403;break;
				case 404:message=HTTP_404;break;
				case 414:message=HTTP_414;break;
				case 500:message=HTTP_500;break;
				case 900:message=HTTP_900;break;
				case 901:message=HTTP_901;break;
				case 902:message=HTTP_902;break;
				case 903:message=HTTP_903;break;
				default:break;
			}
		}
		return message;
	}
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	//获取提示，如果提示为空，则根据statusCode取
	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}
}
