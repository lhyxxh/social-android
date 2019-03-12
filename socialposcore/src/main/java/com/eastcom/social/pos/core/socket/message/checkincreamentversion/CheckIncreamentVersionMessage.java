package com.eastcom.social.pos.core.socket.message.checkincreamentversion;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class CheckIncreamentVersionMessage extends SoMessage {

    private int versionMain;

    private int versionSub;

    public CheckIncreamentVersionMessage(int versionMain, int versionSub) {
        short bodyLength = 4;

        this.versionMain = versionMain;
        this.versionSub = versionSub;

        ByteBuf bf = Unpooled.buffer(bodyLength);
        bf.writeBytes(ByteUtils.intToBytes2(versionMain));
        bf.writeBytes(ByteUtils.intToBytes2(versionSub));

        this.command = SoCommand.校验固件增量包版本;
        this.total = bodyLength;
        this.body = new byte[bodyLength];
        bf.readBytes(body);

        super.computeChecksum();
    }

    public CheckIncreamentVersionMessage(SoMessage message) {
        super(message);

        this.versionMain = ByteUtils.bytes2Int(this.body, 0, 2);
        this.versionSub = ByteUtils.bytes2Int(this.body, 2, 2);
    }

}
