package com.eastcom.social.pos.core.socket.handler.socialinfo;

import io.netty.channel.ChannelHandlerContext;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.socialinfo.SocialInfoRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.socialinfo.SocailInfoRespMessage;

public class ClientSocialInfoHandler extends ClientHandlerBase {

	SocialInfoRespListener socialInfoRespListener;

	public ClientSocialInfoHandler(SocialInfoRespListener socialInfoRespListener) {
		this.socialInfoRespListener = socialInfoRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应提交社保卡信息) {
			handleSocialInfoRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleSocialInfoRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		SocailInfoRespMessage message = new SocailInfoRespMessage(msg);

		System.out.println("Server to SB --> socailinforespmessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (socialInfoRespListener != null) {
			socialInfoRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}