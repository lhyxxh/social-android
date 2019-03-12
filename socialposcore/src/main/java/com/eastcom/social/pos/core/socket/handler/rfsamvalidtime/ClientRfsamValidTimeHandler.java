package com.eastcom.social.pos.core.socket.handler.rfsamvalidtime;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.rfsamvalidtime.RfsamValidTimeRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.rfsamvalidtime.RfsamValidTimeRespMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientRfsamValidTimeHandler extends ClientHandlerBase {

	RfsamValidTimeRespListener rfsamValidTimeRespListener;

	public ClientRfsamValidTimeHandler(RfsamValidTimeRespListener rfsamValidTimeRespListener) {
		this.rfsamValidTimeRespListener = rfsamValidTimeRespListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应请求RFSAM默认有效时长) {
			handleRfsamValidRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleRfsamValidRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		RfsamValidTimeRespMessage message = new RfsamValidTimeRespMessage(msg);

		System.out.println("Server to SB --> rfsamValidTimeRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (rfsamValidTimeRespListener != null) {
			rfsamValidTimeRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}
}