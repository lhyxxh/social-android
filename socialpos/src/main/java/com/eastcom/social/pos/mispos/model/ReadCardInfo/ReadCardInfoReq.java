package com.eastcom.social.pos.mispos.model.ReadCardInfo;

public class ReadCardInfoReq {
    String type="91";   //‘91’-返回
    String insideAuthData;
    String outsideAuthData;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInsideAuthData() {
        return insideAuthData;
    }

    public void setInsideAuthData(String insideAuthData) {
        this.insideAuthData = insideAuthData;
    }

    public String getOutsideAuthData() {
        return outsideAuthData;
    }

    public void setOutsideAuthData(String outsideAuthData) {
        this.outsideAuthData = outsideAuthData;
    }
}
