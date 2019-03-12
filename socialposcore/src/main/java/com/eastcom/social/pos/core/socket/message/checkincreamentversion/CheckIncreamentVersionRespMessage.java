package com.eastcom.social.pos.core.socket.message.checkincreamentversion;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

public class CheckIncreamentVersionRespMessage extends SoMessage {
    private int versionMainUpdate;

    private int versionSubUpdate;

    private int hasIncrePacket;

    private int supportLowestMainVersion;

    private int supportLowestSubVersion;

    public int getHasIncrePacket() {
        return hasIncrePacket;
    }

    public void setHasIncrePacket(int hasIncrePacket) {
        this.hasIncrePacket = hasIncrePacket;
    }

    public int getSupportLowestMainVersion() {
        return supportLowestMainVersion;
    }

    public void setSupportLowestMainVersion(int supportLowestMainVersion) {
        this.supportLowestMainVersion = supportLowestMainVersion;
    }

    public int getSupportLowestSubVersion() {
        return supportLowestSubVersion;
    }

    public void setSupportLowestSubVersion(int supportLowestSubVersion) {
        this.supportLowestSubVersion = supportLowestSubVersion;
    }

    public CheckIncreamentVersionRespMessage(SoMessage message) {
        super(message);

        this.versionMainUpdate = ByteUtils.bytes2Int(this.body, 0, 2);
        this.versionSubUpdate = ByteUtils.bytes2Int(this.body, 2, 2);
        this.hasIncrePacket = ByteUtils.bytes2Int(this.body, 4, 1);
        this.supportLowestMainVersion = ByteUtils.bytes2Int(this.body, 5, 2);
        this.supportLowestSubVersion = ByteUtils.bytes2Int(this.body, 7, 2);
    }


    public int getVersionMainUpdate() {
        return versionMainUpdate;
    }


    public void setVersionMainUpdate(int versionMainUpdate) {
        this.versionMainUpdate = versionMainUpdate;
    }


    public int getVersionSubUpdate() {
        return versionSubUpdate;
    }


    public void setVersionSubUpdate(int versionSubUpdate) {
        this.versionSubUpdate = versionSubUpdate;
    }


}
