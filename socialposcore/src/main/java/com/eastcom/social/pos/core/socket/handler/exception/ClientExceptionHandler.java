package com.eastcom.social.pos.core.socket.handler.exception;

import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.nodata.NoDataRespListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.nodata.NoDataMessage;

import io.netty.channel.ChannelHandlerContext;

/**
 * 异常处理的处理器，放在最后检测
 */
public class ClientExceptionHandler extends ClientHandlerBase {

	NoDataRespListener noDataRespListener;

	public ClientExceptionHandler(NoDataRespListener noDataRespListener) {
		this.noDataRespListener = noDataRespListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null) {
			handleNodataMessage(message, ctx);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// 异常处理
		ctx.close();
	}

	protected void handleNodataMessage(SoMessage msg, ChannelHandlerContext ctx) {
		NoDataMessage message = new NoDataMessage(msg);

		System.out.println("Server to SB --> invalid cmd : ");
		System.out.println(message);
		System.out.println(message.toHexString());

		if (noDataRespListener != null) {
			noDataRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}
}
