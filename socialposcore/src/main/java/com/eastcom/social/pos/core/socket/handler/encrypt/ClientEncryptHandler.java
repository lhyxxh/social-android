package com.eastcom.social.pos.core.socket.handler.encrypt;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.encrypt.EncryptRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.encrypt.EncryptRespMessage;

public class ClientEncryptHandler extends ClientHandlerBase {

	EncryptRespListener encryptRespListener;

	public ClientEncryptHandler(EncryptRespListener encryptRespListener) {
		this.encryptRespListener = encryptRespListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应请求交易数据是否被加密) {
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
		EncryptRespMessage message = new EncryptRespMessage(msg);

		System.out.println("Server to SB --> encryptRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (encryptRespListener != null) {
			encryptRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}
}