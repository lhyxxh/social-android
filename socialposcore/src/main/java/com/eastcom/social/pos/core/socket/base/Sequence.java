package com.eastcom.social.pos.core.socket.base;

public class Sequence {
    int sequence = 0;

    public synchronized int getSequence(){
        if (sequence >= Integer.MAX_VALUE || sequence < 0) {
            sequence = 0;
        }
        return (++sequence);
    }
}
