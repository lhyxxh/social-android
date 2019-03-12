package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0003;

import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;
import com.eastcom.social.pos.core.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class QueryEcardSocialTradeResp {
    private String orderNo; //交易预支付单号 c1
    private Integer status; //支付状态 c2
    private Date payTime;   //支付完成时间 c3
    private Integer socialFee;  //医保个账支付金额 c4
    private Integer channelFee; //渠道支付金额 c5

    private BaseJsonModel baseJsonModel;

    public QueryEcardSocialTradeResp(Map<String , Object> data){
        try {
            baseJsonModel = new BaseJsonModel();
            baseJsonModel.setBusinessSeq((Integer) data.get("t2"));
            baseJsonModel.setBusinessId((String)data.get("t1"));
            this.setOrderNo((String) data.get("c1"));
            this.setStatus((Integer) data.get("c2"));
            this.setPayTime(DateUtils.strToDate((String) data.get("c3")));
            this.setSocialFee((Integer) data.get("c4"));
            this.setChannelFee((Integer) data.get("c5"));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getSocialFee() {
        return socialFee;
    }

    public void setSocialFee(Integer socialFee) {
        this.socialFee = socialFee;
    }

    public Integer getChannelFee() {
        return channelFee;
    }

    public void setChannelFee(Integer channelFee) {
        this.channelFee = channelFee;
    }
}
