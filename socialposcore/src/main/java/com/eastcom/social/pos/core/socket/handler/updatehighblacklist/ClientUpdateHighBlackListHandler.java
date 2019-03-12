package com.eastcom.social.pos.core.socket.handler.updatehighblacklist;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.updatehighblacklist.UpdateHighBlackListRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.updatehighblacklist.UpdateHighBlackListRespMessage;

public class ClientUpdateHighBlackListHandler extends ClientHandlerBase {

	UpdateHighBlackListRespListener updateBlackListRespListener;

	public ClientUpdateHighBlackListHandler(UpdateHighBlackListRespListener updateBlackListRespListener) {
		this.updateBlackListRespListener = updateBlackListRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应请求更新黑名单完整区) {
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
		UpdateHighBlackListRespMessage message = new UpdateHighBlackListRespMessage(msg);

		System.out.println("Server to SB --> UpdateBlackListRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (updateBlackListRespListener != null) {
			updateBlackListRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}
