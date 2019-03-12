package com.eastcom.social.pos.mispos.model.InsideAuthInfo;

public class InsideAuthInfoReq {
    String type= "90"; //‘90’-返回内部认证和外部认证所需信息
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
