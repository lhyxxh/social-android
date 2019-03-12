package com.eastcom.social.pos.core.socket.handler.checkhighblacklist;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.checkhighblacklist.CheckHighBlackListRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.checkhighblacklist.CheckHighBlackListRespMessage;

public class ClientCheckHighBlackListVersionHandler extends ClientHandlerBase {

	CheckHighBlackListRespListener checkHighBlackListRespListener;

	public ClientCheckHighBlackListVersionHandler(CheckHighBlackListRespListener checkHighBlackListRespListener) {
		this.checkHighBlackListRespListener = checkHighBlackListRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应请求校对黑名单版本) {
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
		CheckHighBlackListRespMessage message = new CheckHighBlackListRespMessage(msg);

		System.out.println("Server to SB --> CheckHighBlackListRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (checkHighBlackListRespListener != null) {
			checkHighBlackListRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}
