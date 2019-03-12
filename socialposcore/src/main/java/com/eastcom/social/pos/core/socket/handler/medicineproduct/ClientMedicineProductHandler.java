package com.eastcom.social.pos.core.socket.handler.medicineproduct;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.medicineproduct.MedicineProductRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.medicineproduct.MedicineProductRespMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientMedicineProductHandler extends ClientHandlerBase {

	MedicineProductRespListener medicineProductRespListener;

	public ClientMedicineProductHandler(MedicineProductRespListener medicineProductRespListener) {
		this.medicineProductRespListener = medicineProductRespListener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SoMessage message = (SoMessage) msg;

		if (message != null && message.getCommand() == SoCommand.回应注册药品信息) {
			handleMedicineProductRespMessage(message, ctx);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}

	protected void handleMedicineProductRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
		MedicineProductRespMessage message = new MedicineProductRespMessage(msg);

		System.out.println("Server to SB --> medicineProductRespMessage : ");
		System.out.println(message);
		System.out.println(message.toHexString());

		if (message.validateChecksum() == false) {
			throw new ChecksumErrorException("校验码错误");
		}

		if (medicineProductRespListener != null) {
			medicineProductRespListener.handlerRespMessage(message);
		} else if (messageRespListener != null) {
			messageRespListener.handlerRespMessage(msg);
		}

		SendNotice(message);
	}
}