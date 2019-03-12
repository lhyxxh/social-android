package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0006;

import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;

import java.util.Map;

public class SubmitEcardSocialTradeResp {
    private Integer status; //保存状态 c1
    private String orderNo; //交易预支付单号 c2
    private String refundOrderNo; //退款单号 c3

    private BaseJsonModel baseJsonModel;

    public SubmitEcardSocialTradeResp(Map<String, Object> data) {
        baseJsonModel = new BaseJsonModel();
        baseJsonModel.setBusinessSeq((Integer) data.get("t2"));
        baseJsonModel.setBusinessId((String)data.get("t1"));
        this.setStatus((Integer) data.get("c1"));
        this.setOrderNo((String) data.get("c2"));
        this.setRefundOrderNo((String) data.get("c3"));
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo;
    }
}
