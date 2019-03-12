package com.eastcom.social.pos.core.socket.handler.queryhealth;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.queryhealth.QueryHealthRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.queryhealth.QueryHealthRespMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientQueryHealthHandler extends ClientHandlerBase {

	QueryHealthRespListener queryHealthRespListener;

	public ClientQueryHealthHandler(QueryHealthRespListener queryHealthRespListener) {
		this.queryHealthRespListener = queryHealthRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应查询保健品信息) {
			handleQueryHealthRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleQueryHealthRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		QueryHealthRespMessage message = new QueryHealthRespMessage(msg);

		System.out.println("Server to SB --> queryHealthRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (queryHealthRespListener != null) {
			queryHealthRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}
