package com.eastcom.social.pos.core.socket.handler.checkpolicyversion;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.checkpolicyversion.CheckPolicyVersionRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

public class ClientCheckPolicyVersionHandler extends ClientHandlerBase {

	CheckPolicyVersionRespListener checkPolicyVersionRespListener;

	public ClientCheckPolicyVersionHandler(CheckPolicyVersionRespListener checkPolicyVersionRespListener) {
		this.checkPolicyVersionRespListener = checkPolicyVersionRespListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应政策文件下载确认) {
			handleCheckPolicyVersionRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleCheckPolicyVersionRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		System.out.println("Server to SB --> checkVersionRespMessage : ");
		System.out.println(msg);
		System.out.println(msg.toHexString());

		if (checkPolicyVersionRespListener != null) {
			checkPolicyVersionRespListener.handlerRespMessage(msg);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(msg);
	}
}
