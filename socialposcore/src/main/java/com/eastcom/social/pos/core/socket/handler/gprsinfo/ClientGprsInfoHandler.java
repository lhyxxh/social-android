package com.eastcom.social.pos.core.socket.handler.gprsinfo;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.gprsinfo.GprsInfoRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.gprsinfo.GprsInfoRespMessage;

public class ClientGprsInfoHandler extends ClientHandlerBase {

	GprsInfoRespListener gprsInfoRespListener;

	public ClientGprsInfoHandler(GprsInfoRespListener gprsInfoRespListener) {
		this.gprsInfoRespListener = gprsInfoRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应提交GPRS定位信息) {
			handleGprsRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleGprsRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		GprsInfoRespMessage message = new GprsInfoRespMessage(msg);

		System.out.println("Server to SB --> gprsRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (gprsInfoRespListener != null) {
			gprsInfoRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}