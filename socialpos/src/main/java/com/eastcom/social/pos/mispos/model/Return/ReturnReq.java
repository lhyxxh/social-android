package com.eastcom.social.pos.mispos.model.Return;

public class ReturnReq {
    String type = "03"; //‘03’-退货
    String shNo;
    String posNo;
    String shName;
    String engShName;
    String money;
    String tradeCkNo;
    String tradeDate;
    String twoTape;
    String threeTape;
    String opearator;
    String attachInfo;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShNo() {
        return shNo;
    }

    public void setShNo(String shNo) {
        this.shNo = shNo;
    }

    public String getPosNo() {
        return posNo;
    }

    public void setPosNo(String posNo) {
        this.posNo = posNo;
    }

    public String getShName() {
        return shName;
    }

    public void setShName(String shName) {
        this.shName = shName;
    }

    public String getEngShName() {
        return engShName;
    }

    public void setEngShName(String engShName) {
        this.engShName = engShName;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTradeCkNo() {
        return tradeCkNo;
    }

    public void setTradeCkNo(String tradeCkNo) {
        this.tradeCkNo = tradeCkNo;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTwoTape() {
        return twoTape;
    }

    public void setTwoTape(String twoTape) {
        this.twoTape = twoTape;
    }

    public String getThreeTape() {
        return threeTape;
    }

    public void setThreeTape(String threeTape) {
        this.threeTape = threeTape;
    }

    public String getOpearator() {
        return opearator;
    }

    public void setOpearator(String opearator) {
        this.opearator = opearator;
    }

    public String getAttachInfo() {
        return attachInfo;
    }

    public void setAttachInfo(String attachInfo) {
        this.attachInfo = attachInfo;
    }
}
