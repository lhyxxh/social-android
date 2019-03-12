package com.eastcom.social.pos.mispos.model.RePrint;

public class RePrintReq {
    String type = "05";
    String lastTradeCkNo;
    String lastTradeLsNo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLastTradeCkNo() {
        return lastTradeCkNo;
    }

    public void setLastTradeCkNo(String lastTradeCkNo) {
        this.lastTradeCkNo = lastTradeCkNo;
    }

    public String getLastTradeLsNo() {
        return lastTradeLsNo;
    }

    public void setLastTradeLsNo(String lastTradeLsNo) {
        this.lastTradeLsNo = lastTradeLsNo;
    }
}
