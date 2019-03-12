package com.eastcom.social.pos.core.socket.handler.querymedicine;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.querymedicine.QueryMedicineRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.querymedicine.QueryMedicineRespMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientQueryMedicineHandler extends ClientHandlerBase {

	QueryMedicineRespListener queryMedicineRespListener;

	public ClientQueryMedicineHandler(QueryMedicineRespListener queryMedicineRespListener) {
		this.queryMedicineRespListener = queryMedicineRespListener;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应查询药品信息) {
			handleQueryMedicineRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleQueryMedicineRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		QueryMedicineRespMessage message = new QueryMedicineRespMessage(msg);

		System.out.println("Server to SB --> queryMedicineRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (queryMedicineRespListener != null) {
			queryMedicineRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}

}
