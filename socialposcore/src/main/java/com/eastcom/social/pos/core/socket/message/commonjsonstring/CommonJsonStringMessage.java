package com.eastcom.social.pos.core.socket.message.commonjsonstring;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class CommonJsonStringMessage extends SoMessage {

    String jsonData;

    public CommonJsonStringMessage(String data) {
        byte[] bytes = data.getBytes();
        short bodyLength = (short) bytes.length;

        this.jsonData = data;

        ByteBuf bf = Unpooled.buffer(bodyLength);
        bf.writeBytes(data.getBytes());

        this.command = SoCommand.通用json字符串指令;
        this.total = bodyLength;
        this.body = new byte[bodyLength];
        bf.readBytes(body);

        super.computeChecksum();
    }

    public CommonJsonStringMessage(SoMessage message) {
        super(message);
        this.jsonData = new String(this.body, CHARSET_UTF8);
    }

    @Override
    public String toString() {
        return super.toString() + "\r\n" + "body [jsonData=" + jsonData + "]";
    }
}
