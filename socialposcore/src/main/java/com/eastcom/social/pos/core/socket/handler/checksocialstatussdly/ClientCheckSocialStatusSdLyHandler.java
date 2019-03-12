package com.eastcom.social.pos.core.socket.handler.checksocialstatussdly;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.checksocialstatussdly.CheckSocialStatusSdLyRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.checksocialstatussdly.CheckSocialStatusSdLyRespMessage;

public class ClientCheckSocialStatusSdLyHandler extends ClientHandlerBase {

	CheckSocialStatusSdLyRespListener checkSocialStatusSdLyRespListener;

	public ClientCheckSocialStatusSdLyHandler(CheckSocialStatusSdLyRespListener checkSocialStatusSdLyRespListener) {
		this.checkSocialStatusSdLyRespListener = checkSocialStatusSdLyRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应查询临沂社保卡状态) {
			handleUpdateBlackListRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleUpdateBlackListRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		CheckSocialStatusSdLyRespMessage message = new CheckSocialStatusSdLyRespMessage(msg);

		System.out.println("Server to SB --> CheckSocialStatusSdLyRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (checkSocialStatusSdLyRespListener != null) {
			checkSocialStatusSdLyRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}
