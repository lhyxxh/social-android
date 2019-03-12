package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0001;

import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;

import java.util.Map;

public class SubmitEcardQrcodeResp {
    private Integer status; //查询状态 c1
    private String sbCardNo;    //社保卡号 c2
    private Integer sbCardStatus;   //社保卡状态 c3
    private String idCardNo;    //身份证号 c4
    private String name;    //姓名 c5
    private String bankCardNo;    //医保个人账户 c6
    private String sbCardCityNo;    //社保卡所属城市代码 c7
    private String sbCardCode;  //社保卡卡识别码 c8
    private String orderNo; //交易预支付单号 c9
    private Integer channelType;    //二维码渠道来源 c10
    private String sbCardReset; //社保卡卡片复位信息 c11

    private Integer balance; //医保个人账户余额 c12

    private BaseJsonModel baseJsonModel;

    public SubmitEcardQrcodeResp(Map<String , Object> data){
        baseJsonModel = new BaseJsonModel();
        baseJsonModel.setBusinessSeq((Integer) data.get("t2"));
        baseJsonModel.setBusinessId((String)data.get("t1"));
        this.setStatus((Integer) data.get("c1"));
        this.setSbCardNo((String) data.get("c2"));
        this.setSbCardStatus((Integer) data.get("c3"));
        this.setIdCardNo((String) data.get("c4"));
        this.setName((String) data.get("c5"));
        this.setBankCardNo((String) data.get("c6"));
        this.setSbCardCityNo((String) data.get("c7"));
        this.setSbCardCode((String) data.get("c8"));
        this.setOrderNo((String) data.get("c9"));
        this.setChannelType((Integer) data.get("c10"));
        this.setSbCardReset((String) data.get("c11"));
        this.setBalance((Integer) data.get("c12"));
    }

    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getSbCardNo() {
        return sbCardNo;
    }

    public void setSbCardNo(String sbCardNo) {
        this.sbCardNo = sbCardNo;
    }

    public Integer getSbCardStatus() {
        return sbCardStatus;
    }

    public void setSbCardStatus(Integer sbCardStatus) {
        this.sbCardStatus = sbCardStatus;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getSbCardCityNo() {
        return sbCardCityNo;
    }

    public void setSbCardCityNo(String sbCardCityNo) {
        this.sbCardCityNo = sbCardCityNo;
    }

    public String getSbCardCode() {
        return sbCardCode;
    }

    public void setSbCardCode(String sbCardCode) {
        this.sbCardCode = sbCardCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public String getSbCardReset() {
        return sbCardReset;
    }

    public void setSbCardReset(String sbCardReset) {
        this.sbCardReset = sbCardReset;
    }

}
