package com.eastcom.social.pos.core.socket.handler.updatelowblacklist;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.updatelowblacklist.UpdateLowBlackListRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.updatelowblacklist.UpdateLowBlackListRespMessage;

public class ClientUpdateLowBlackListHandler extends ClientHandlerBase {

	UpdateLowBlackListRespListener updateLowBlackListRespListener;

	public ClientUpdateLowBlackListHandler(UpdateLowBlackListRespListener updateLowBlackListRespListener) {
		this.updateLowBlackListRespListener = updateLowBlackListRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应请求更新黑名单编辑区) {
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
		UpdateLowBlackListRespMessage message = new UpdateLowBlackListRespMessage(msg);

		System.out.println("Server to SB --> UpdateBlackListRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (updateLowBlackListRespListener != null) {
			updateLowBlackListRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}
