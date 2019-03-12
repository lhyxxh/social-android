package com.eastcom.social.pos.core.socket.handler.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.heartbeat.HeartbeatRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.heartbeat.HeartbeatRespMessage;
import com.eastcom.social.pos.core.socket.task.ClientHeartbeatTask;

public class ClientHeartbeatHandler extends ClientHandlerBase {

	HeartbeatRespListener heartbeatRespListener;
	
	private SoClient mSoClient;


	public void setmSoClient(SoClient soClient) {
		this.mSoClient = soClient;
	}

	public ClientHeartbeatHandler(HeartbeatRespListener heartbeatRespListener) {
		this.heartbeatRespListener = heartbeatRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
	}
	
	/**
	 * 在指定时间内没有读写指令，重新发送心跳指令
	 */
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			ctx.executor().execute(new ClientHeartbeatTask(ctx));
		}

	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应心跳包) {
			handleHeartbeatRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

		if (clientHeartbeatTask != null) {
			clientHeartbeatTask.cancel(true);
			clientHeartbeatTask = null;
			System.out.println("clientHeartbeatTask close");
		}

		ctx.fireExceptionCaught(cause);
	}

	/**
	 * 处理心跳响应消息
	 * 
	 * @param msg
	 * @param ctx
	 * @throws Exception
	 */
	protected void handleHeartbeatRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		HeartbeatRespMessage message = new HeartbeatRespMessage(msg);

		System.out.println("Server to SB --> handleHeartbeatRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (heartbeatRespListener != null) {
			heartbeatRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
		
	}
}
