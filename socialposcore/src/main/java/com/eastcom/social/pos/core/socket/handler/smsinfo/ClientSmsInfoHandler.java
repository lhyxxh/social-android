package com.eastcom.social.pos.core.socket.handler.smsinfo;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.smsinfo.SmsInfoRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.smsinfo.SmsInfoRespMessage;

public class ClientSmsInfoHandler extends ClientHandlerBase {

	SmsInfoRespListener smsInfoRespListener;

	public ClientSmsInfoHandler(SmsInfoRespListener smsInfoRespListener) {
		this.smsInfoRespListener = smsInfoRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应获取系统短通知) {
			handleSmsInfoRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleSmsInfoRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		SmsInfoRespMessage message = new SmsInfoRespMessage(msg);

		System.out.println("Server to SB --> SmsInfoRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (smsInfoRespListener != null) {
			smsInfoRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}