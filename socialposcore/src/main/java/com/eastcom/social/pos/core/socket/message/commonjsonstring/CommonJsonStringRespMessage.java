package com.eastcom.social.pos.core.socket.message.commonjsonstring;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class CommonJsonStringRespMessage extends SoMessage {

    String jsonData;

    public CommonJsonStringRespMessage(String data) {
        short bodyLength = 4;

        this.jsonData = jsonData;

        ByteBuf bf = Unpooled.buffer(bodyLength);
        bf.writeBytes(data.getBytes());

        this.command = SoCommand.通用json字符串指令;
        this.total = bodyLength;
        this.body = new byte[bodyLength];
        bf.readBytes(body);

        super.computeChecksum();
    }

    public CommonJsonStringRespMessage(SoMessage message) {
        super(message);
        this.jsonData = new String(this.body, CHARSET_UTF8);
    }

    @Override
    public String toString() {
        return super.toString() + "\r\n" + "body [jsonData=" + jsonData + "]";
    }

    public String getJsonData() {
        return jsonData;
    }
}
