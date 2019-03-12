package com.eastcom.social.pos.core.socket.handler.healthproduct;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.healthproduct.HealthProductRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.healthproduct.HealthProductRespMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientHealthProductHandler extends ClientHandlerBase {

	HealthProductRespListener healthProductRespListener;

	public ClientHealthProductHandler(HealthProductRespListener healthProductRespListener) {
		this.healthProductRespListener = healthProductRespListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应注册保健品信息) {
			handleHealthProductRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleHealthProductRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		HealthProductRespMessage message = new HealthProductRespMessage(msg);

		System.out.println("Server to SB --> healthProductRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (healthProductRespListener != null) {
			healthProductRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}
}
