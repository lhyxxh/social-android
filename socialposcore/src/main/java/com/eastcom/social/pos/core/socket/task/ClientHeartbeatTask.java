package com.eastcom.social.pos.core.socket.task;

import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.message.SoFollowCommad;
import com.eastcom.social.pos.core.socket.message.heartbeat.HeartbeatMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientHeartbeatTask implements Runnable {
	private final ChannelHandlerContext ctx;

	public ClientHeartbeatTask(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void run() {
		sendHeartbeatMessage(ctx);
	}

	protected HeartbeatMessage buildHeartbeatMessage(ChannelHandlerContext ctx) {
		String signboard = ctx.channel().attr(SoClient.KEY_CONFIG).get().getSignboard();
		byte fcmd = SoFollowCommad.没有后续动作;
		HeartbeatMessage message = new HeartbeatMessage(signboard, fcmd);

		return message;
	}

	/**
	 * 发送心跳消息
	 * 
	 * @param ctx
	 * @throws Exception
	 */
	protected void sendHeartbeatMessage(ChannelHandlerContext ctx) {

		HeartbeatMessage message = buildHeartbeatMessage(ctx);

		ctx.writeAndFlush(message);

		System.out.println("SB to Server --> sendHeartbeatMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());
		System.out.println();
	}
}
