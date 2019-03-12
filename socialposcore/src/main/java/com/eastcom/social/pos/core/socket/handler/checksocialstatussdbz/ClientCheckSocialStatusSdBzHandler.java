package com.eastcom.social.pos.core.socket.handler.checksocialstatussdbz;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.checksocialstatussdbz.CheckSocialStatusSdBzRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.checksocialstatussdbz.CheckSocialStatusSdBzRespMessage;

public class ClientCheckSocialStatusSdBzHandler extends ClientHandlerBase {

	CheckSocialStatusSdBzRespListener checkSocialStatusRespListener;

	public ClientCheckSocialStatusSdBzHandler(CheckSocialStatusSdBzRespListener checkSocialStatusRespListener) {
		this.checkSocialStatusRespListener = checkSocialStatusRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应查询社保卡状态) {
			handleCheckSocialStatusSdBzRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleCheckSocialStatusSdBzRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		CheckSocialStatusSdBzRespMessage message = new CheckSocialStatusSdBzRespMessage(msg);

		System.out.println("Server to SB --> CheckSocialStatusRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (checkSocialStatusRespListener != null) {
			checkSocialStatusRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}
