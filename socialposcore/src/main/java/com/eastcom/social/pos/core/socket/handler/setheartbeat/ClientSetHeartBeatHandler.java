package com.eastcom.social.pos.core.socket.handler.setheartbeat;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.setheartbeat.SetHeartBeatRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.setheartbeat.SetHeartBeatRespMessage;

public class ClientSetHeartBeatHandler extends ClientHandlerBase {

	SetHeartBeatRespListener setHeartBeatRespListener;

	public ClientSetHeartBeatHandler(SetHeartBeatRespListener setHeartBeatRespListener) {
		this.setHeartBeatRespListener = setHeartBeatRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.请求心跳频率) {
			handleTradeRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleTradeRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		SetHeartBeatRespMessage message = new SetHeartBeatRespMessage(msg);

		System.out.println("Server to SB --> setHeartBeatRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (setHeartBeatRespListener != null) {
			setHeartBeatRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}