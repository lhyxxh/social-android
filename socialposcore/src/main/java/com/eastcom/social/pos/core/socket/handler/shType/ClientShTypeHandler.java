package com.eastcom.social.pos.core.socket.handler.shType;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.shType.ShTypeRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.shType.ShTypeRespMessage;

public class ClientShTypeHandler extends ClientHandlerBase {

	ShTypeRespListener shTypeRespListener;

	public ClientShTypeHandler(ShTypeRespListener shTypeRespListener) {
		this.shTypeRespListener = shTypeRespListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应定点机构类型) {
			handleRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		ShTypeRespMessage message = new ShTypeRespMessage(msg);

		System.out.println("Server to SB --> encryptRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (shTypeRespListener != null) {
			shTypeRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}
}