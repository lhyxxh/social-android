package com.eastcom.social.pos.mispos.model.Revoke;

public class RevokeReq {
    String type = "02"; //‘02’-撤销
    String shNo;
    String posNo;
    String shName;
    String engShName;
    String money;
    String tradeLsNo;
    String twoTape;
    String threeTape;
    String operatorNo;
    String attachInfo; //医保交易流水号|外部认证鉴别数据（32 位）) |

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

    public String getTradeLsNo() {
        return tradeLsNo;
    }

    public void setTradeLsNo(String tradeLsNo) {
        this.tradeLsNo = tradeLsNo;
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

    public String getOperatorNo() {
        return operatorNo;
    }

    public void setOperatorNo(String operatorNo) {
        this.operatorNo = operatorNo;
    }

    public String getAttachInfo() {
        return attachInfo;
    }

    public void setAttachInfo(String attachInfo) {
        this.attachInfo = attachInfo;
    }
}
