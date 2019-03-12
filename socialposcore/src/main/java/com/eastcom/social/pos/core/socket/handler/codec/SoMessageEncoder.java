package com.eastcom.social.pos.core.socket.handler.codec;

import com.eastcom.social.pos.core.socket.message.SoMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 * 
 * @author XZK
 *
 */
public class SoMessageEncoder extends MessageToByteEncoder<SoMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, SoMessage msg, ByteBuf out) throws Exception {
		if (msg == null) {
			throw new Exception("encode message is null");
		}

		out.writeInt(msg.getCommand());
		out.writeShort(msg.getTotal());
		if (msg.getTotal() > 0) {
			out.writeBytes(msg.getBody());
		}
		out.writeByte(msg.getChecksum());
		out.writeByte(msg.getEnd());
	}

}
