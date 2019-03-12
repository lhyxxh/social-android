package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0005;


import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;
import com.eastcom.social.pos.core.utils.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public class QueryEcardSocialTradeRefundResp {
    private String orderNo; //交易预支付单号 c1
    private Integer status; //退款状态 c2
    private Date refundTime;   //退款完成时间 c3
    private Integer refundSocialFee;    //医保个账退款金额 c4
    private Integer refundChannelFee;   //渠道退款金额 c5
    private String refundOrderNo; //退款单号 c6

    private BaseJsonModel baseJsonModel;

    public QueryEcardSocialTradeRefundResp(Map<String, Object> data) {
        try {
            baseJsonModel = new BaseJsonModel();
            baseJsonModel.setBusinessSeq((Integer) data.get("t2"));
            baseJsonModel.setBusinessId((String)data.get("t1"));
            this.setOrderNo((String) data.get("c1"));
            this.setStatus((Integer) data.get("c2"));
            this.setRefundTime(DateUtils.strToDate((String) data.get("c3")));
            this.setRefundSocialFee((Integer) data.get("c4"));
            this.setRefundChannelFee((Integer) data.get("c5"));
            this.setRefundOrderNo((String) data.get("c6"));
        } catch (ParseException e) {
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

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Integer getRefundSocialFee() {
        return refundSocialFee;
    }

    public void setRefundSocialFee(Integer refundSocialFee) {
        this.refundSocialFee = refundSocialFee;
    }

    public Integer getRefundChannelFee() {
        return refundChannelFee;
    }

    public void setRefundChannelFee(Integer refundChannelFee) {
        this.refundChannelFee = refundChannelFee;
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo;
    }
}
