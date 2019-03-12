package com.eastcom.social.pos.core.socket.handler.checkIncreamentVersion;

import com.eastcom.social.pos.core.socket.exception.ChecksumErrorException;
import com.eastcom.social.pos.core.socket.handler.ClientHandlerBase;
import com.eastcom.social.pos.core.socket.listener.CheckIncreamentVersion.CheckIncreamentVersionRespListener;
import com.eastcom.social.pos.core.socket.listener.checkversion.CheckVersionRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.checkincreamentversion.CheckIncreamentVersionRespMessage;
import com.eastcom.social.pos.core.socket.message.checkversion.CheckVersionRespMessage;

import io.netty.channel.ChannelHandlerContext;

public class ClientCheckIncreamentVersionHandler extends ClientHandlerBase {

    CheckIncreamentVersionRespListener checkIncreamentVersionRespListener;

    public ClientCheckIncreamentVersionHandler(CheckIncreamentVersionRespListener checkIncreamentVersionRespListener) {
        this.checkIncreamentVersionRespListener = checkIncreamentVersionRespListener;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SoMessage message = (SoMessage) msg;

        if (message != null && message.getCommand() == SoCommand.回应校验固件增量包版本) {
            handleIncreamentVersionRespMessage(message, ctx);

        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.fireExceptionCaught(cause);
    }

    protected void handleIncreamentVersionRespMessage(SoMessage msg, ChannelHandlerContext ctx) throws Exception {
        CheckIncreamentVersionRespMessage message = new CheckIncreamentVersionRespMessage(msg);

        System.out.println("Server to SB --> checkVersionRespMessage : ");
        System.out.println(message);
        System.out.println(message.toHexString());

        if (message.validateChecksum() == false) {
            throw new ChecksumErrorException("校验码错误");
        }

        if (checkIncreamentVersionRespListener != null) {
            checkIncreamentVersionRespListener.handlerRespMessage(message);
        } else if (messageRespListener != null) {
            messageRespListener.handlerRespMessage(msg);
        }

        SendNotice(message);
    }
}
