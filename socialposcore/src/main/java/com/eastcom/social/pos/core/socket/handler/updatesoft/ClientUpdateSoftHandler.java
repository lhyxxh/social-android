package com.eastcom.social.pos.core.socket.handler.updatesoft;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.updatesoft.UpdateSoftRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.updatesoft.UpdateSoftRespMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientUpdateSoftHandler extends ClientHandlerBase {

	UpdateSoftRespListener updateSoftRespListener;

	public ClientUpdateSoftHandler(UpdateSoftRespListener updateSoftRespListener) {
		this.updateSoftRespListener = updateSoftRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应提交固件版本指令) {
			handleUpdateSoftRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleUpdateSoftRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		UpdateSoftRespMessage message = new UpdateSoftRespMessage(msg);

		System.out.println("Server to SB --> updateSoftRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (updateSoftRespListener != null) {
			updateSoftRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}
