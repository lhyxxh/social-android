package com.eastcom.social.pos.core.socket.handler.checkversion;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.checkversion.CheckVersionRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.checkversion.CheckVersionRespMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientCheckVersionHandler extends ClientHandlerBase {

	CheckVersionRespListener checkVersionRespListener;

	public ClientCheckVersionHandler(CheckVersionRespListener checkVersionRespListener) {
		this.checkVersionRespListener = checkVersionRespListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应校验安卓固件版本) {
			handleCheckVersionRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleCheckVersionRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		CheckVersionRespMessage message = new CheckVersionRespMessage(msg);

		System.out.println("Server to SB --> checkVersionRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (checkVersionRespListener != null) {
			checkVersionRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}
}
