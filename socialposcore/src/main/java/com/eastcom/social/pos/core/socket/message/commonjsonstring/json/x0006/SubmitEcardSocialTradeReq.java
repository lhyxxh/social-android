package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0006;

import com.eastcom.social.pos.core.socket.base.Sequence;
import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;
import com.eastcom.social.pos.core.utils.JsonUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubmitEcardSocialTradeReq {
    private String orderNo; //交易预支付单号 c1
    private String refundOrderNo; //交易预支付单号 c2
    private Integer tradeType;  //交易类型 c3 1支付;2退款
    private Date tradeTime;     //交易完成时间 c4
    private Integer socialFee;    //医保个账交易金额 c5
    private String refNo;   //参考号 c6
    private BaseJsonModel baseJsonModel;
    volatile Sequence sequence;

    public SubmitEcardSocialTradeReq(String orderNo, String refundOrderNo, Integer tradeType, Date tradeTime, Integer socialFee, String refNo) {
        this.orderNo = orderNo;
        this.refundOrderNo = refundOrderNo;
        this.tradeType = tradeType;
        this.tradeTime = tradeTime;
        this.socialFee = socialFee;
        this.refNo = refNo;
        sequence = new Sequence();
        baseJsonModel = new BaseJsonModel();
        baseJsonModel.setBusinessId("0006");
        baseJsonModel.setBusinessSeq(sequence.getSequence());
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

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public Integer getSocialFee() {
        return socialFee;
    }

    public void setSocialFee(Integer socialFee) {
        this.socialFee = socialFee;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    @Override
    public String toString() {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("t1", baseJsonModel.getBusinessId());
            data.put("t2", baseJsonModel.getBusinessSeq());
            data.put("c1", this.getOrderNo());
            data.put("c2", this.getRefundOrderNo());
            data.put("c3", this.getTradeType());
            data.put("c4", this.getTradeTime());
            data.put("c5", this.getSocialFee());
            data.put("c6", this.getRefNo());
            return JsonUtils.toJSONString(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
