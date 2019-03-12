package com.eastcom.social.pos.core.socket.handler.checksocialstatusgdzh;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.checksocialstatusgdzh.CheckSocialStatusGdzhRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.checksocialstatusgdzh.CheckSocialStatusGdzhRespMessage;

public class ClientCheckSocialStatusGdzhHandler extends ClientHandlerBase {

	CheckSocialStatusGdzhRespListener checkSocialStatusGdzhRespListener;

	public ClientCheckSocialStatusGdzhHandler(CheckSocialStatusGdzhRespListener checkSocialStatusGdzhRespListener) {
		this.checkSocialStatusGdzhRespListener = checkSocialStatusGdzhRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应查询珠海社保卡状态) {
			handleCheckSocialStatusGdzhRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleCheckSocialStatusGdzhRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		CheckSocialStatusGdzhRespMessage message = new CheckSocialStatusGdzhRespMessage(msg);

		System.out.println("Server to SB --> CheckSocialStatusGdzhRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (checkSocialStatusGdzhRespListener != null) {
			checkSocialStatusGdzhRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}
