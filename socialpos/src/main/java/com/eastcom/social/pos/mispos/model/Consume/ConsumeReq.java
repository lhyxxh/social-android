package com.eastcom.social.pos.mispos.model.Consume;

public class ConsumeReq {
    String type = "01";
    String shNo;  //商户号
    String posNo; //终端号
    String shName; //商户名
    String engShName; //英文商户名
    String money;   //金额12 字节，无小数点，左补 0，单位：分
    String twoTape; //二磁信息
    String threeTape; //三磁信息
    String operatorNo; //操作员号
    String attachInfo;  //附件信息  医保交易流水号|外部认证鉴别数据（32 位）) |

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
