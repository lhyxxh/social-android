package com.eastcom.social.pos.core.socket.handler.rfsamlist;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.rfsamlist.RfsamListRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.rfsamlist.RfsamListRespMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientRfsamListHandler extends ClientHandlerBase {

	RfsamListRespListener rfsamListRespListener;

	public ClientRfsamListHandler(RfsamListRespListener rfsamListRespListener) {
		this.rfsamListRespListener = rfsamListRespListener;
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应请求RFSAM卡名单) {
			handleRfsamListRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleRfsamListRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		RfsamListRespMessage message = new RfsamListRespMessage(msg);

		System.out.println("Server to SB --> rfsamListRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (rfsamListRespListener != null) {
			rfsamListRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}
}