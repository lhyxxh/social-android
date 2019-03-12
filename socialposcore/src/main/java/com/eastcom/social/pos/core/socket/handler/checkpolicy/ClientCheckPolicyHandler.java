package com.eastcom.social.pos.core.socket.handler.checkpolicy;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.checkpolicy.CheckPolicyRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class ClientCheckPolicyHandler extends ClientHandlerBase {

	CheckPolicyRespListener checkPolicyRespListener;

	public ClientCheckPolicyHandler(CheckPolicyRespListener checkPolicyRespListener) {
		this.checkPolicyRespListener = checkPolicyRespListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应请求政策文件基本信息
				) {
			handleCheckPolicyRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleCheckPolicyRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {

		System.out.println("Server to SB --> CheckPolicyRespMessage : ");
		System.out.println(msg);
		System.out.println(msg.toHexString());

		if (msg.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (checkPolicyRespListener != null) {
			checkPolicyRespListener.handlerRespMessage(msg);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(msg);
	}
}
