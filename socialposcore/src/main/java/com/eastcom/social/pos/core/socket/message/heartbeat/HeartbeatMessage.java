package com.eastcom.social.pos.core.socket.message.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class HeartbeatMessage extends SoMessage {
	
	private byte followCommand; //1字节 后续指令
	
	public HeartbeatMessage(String signboard, byte followCommand) {
		short bodyLength = SB_LENGTH + 1;
	
		this.signboard = signboard;
		this.followCommand = followCommand;

		ByteBuf bf = Unpooled.buffer(bodyLength);
		bf.writeBytes(signboardToBytes());
		bf.writeByte(this.followCommand);
		
		this.command = SoCommand.发送心跳包;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		bf.readBytes(body);
		
		super.computeChecksum();
	}
	
	public HeartbeatMessage(SoMessage message){
		super(message);
		
		ByteBuf bf = Unpooled.copiedBuffer(this.body);
		this.signboard = bf.readCharSequence(SB_LENGTH, CHARSET).toString();
		this.followCommand = bf.readByte();
	}
	
	@Override
	public String toString(){
		return super.toString() + "\r\n" 
				+ "body [signboard=" + signboard 
				+ ",followCommand=" + ByteUtils.toHexString(followCommand)
				+ "]";
	}
}
