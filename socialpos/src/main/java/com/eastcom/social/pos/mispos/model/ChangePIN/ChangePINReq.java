package com.eastcom.social.pos.mispos.model.ChangePIN;

public class ChangePINReq {
    String type = "95";  //'95'-修改 PIN

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
