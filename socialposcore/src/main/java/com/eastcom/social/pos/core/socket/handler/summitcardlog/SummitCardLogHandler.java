package com.eastcom.social.pos.core.socket.handler.summitcardlog;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.summitcardlog.SummitCardLogRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.summitcardlog.SummitCardLogRespMessage;

public class SummitCardLogHandler extends ClientHandlerBase {

	SummitCardLogRespListener changePwdRespListener;

	public SummitCardLogHandler(SummitCardLogRespListener changePwdRespListener) {
		this.changePwdRespListener = changePwdRespListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应提交社保卡操作记录) {
			handleCheckVersionRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleCheckVersionRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		SummitCardLogRespMessage message = new SummitCardLogRespMessage(msg);

		System.out.println("Server to SB --> changePwdRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (changePwdRespListener != null) {
			changePwdRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}
}
