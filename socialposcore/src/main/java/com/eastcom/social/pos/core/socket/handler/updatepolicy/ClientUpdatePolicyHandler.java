package com.eastcom.social.pos.core.socket.handler.updatepolicy;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.updatepolicy.UpdatePolicyRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.updatepolicy.UpdatePolicyRespMessage;

public class ClientUpdatePolicyHandler extends ClientHandlerBase {

	UpdatePolicyRespListener updatePolicyRespListener;

	public ClientUpdatePolicyHandler(UpdatePolicyRespListener updatePolicyRespListener) {
		this.updatePolicyRespListener = updatePolicyRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应请求政策文件数据包) {
			handleUpdatePolicyRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleUpdatePolicyRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		UpdatePolicyRespMessage message = new UpdatePolicyRespMessage(msg);

		System.out.println("Server to SB --> updateSoftRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (updatePolicyRespListener != null) {
			updatePolicyRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}
