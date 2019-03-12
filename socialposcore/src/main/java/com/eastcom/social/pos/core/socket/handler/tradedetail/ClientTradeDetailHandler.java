package com.eastcom.social.pos.core.socket.handler.tradedetail;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.tradedetail.TradeDetailRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.tradedetail.TradeDetailRespMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientTradeDetailHandler extends ClientHandlerBase {

	TradeDetailRespListener tradeDetailRespListener;

	public ClientTradeDetailHandler(TradeDetailRespListener tradeDetailRespListener) {
		this.tradeDetailRespListener = tradeDetailRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应上传交易明细) {
			handleTradeDetailRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleTradeDetailRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		TradeDetailRespMessage message = new TradeDetailRespMessage(msg);

		System.out.println("Server to SB --> tradeDetailRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (tradeDetailRespListener != null) {
			tradeDetailRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}
