package com.eastcom.social.pos.core.socket.handler.confirm;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.confirm.ConfirmRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.confirm.ConfirmRespMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientConfirmHandler extends ClientHandlerBase {

	ConfirmRespListener confirmRespListener;

	public ClientConfirmHandler(ConfirmRespListener confirmRespListener) {
		this.confirmRespListener = confirmRespListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应指令确认) {
			handleConfirmRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleConfirmRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		ConfirmRespMessage message = new ConfirmRespMessage(msg);

		System.out.println("Server to SB --> confirmRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (confirmRespListener != null) {
			confirmRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}
}