package com.eastcom.social.pos.core.socket.handler.codec;

import com.eastcom.social.pos.core.socket.message.SoMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/***
 * 解码器(包括对半包和粘包进行处理)
 * 
 * @author XZK
 *
 */
public class SoMessageDecoder extends LengthFieldBasedFrameDecoder {

	public SoMessageDecoder() {
		super(10240, 4, 2, 2, 0);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf bf = (ByteBuf) super.decode(ctx, in);
		if (bf == null) {
			return null;
		}

		SoMessage message = new SoMessage();
		message.setCommand(bf.readInt());
		message.setTotal(bf.readShort());
		if (message.getTotal() > 0) {
			byte[] body = new byte[message.getTotal()];
			bf.readBytes(body);
			message.setBody(body);
		}
		message.setChecksum(bf.readByte());
		message.setEnd(bf.readByte());

		return message;
	}

}
