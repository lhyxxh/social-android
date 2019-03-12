package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0004;

import com.eastcom.social.pos.core.socket.base.Sequence;
import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;
import com.eastcom.social.pos.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class LaunchEcardSocialTradeRefundReq {
    private String orderNo; //交易预支付单号 c1
    private Integer refundFee; //退款金额 c2
    private String refundOrderNo; //退款单号 c3
    private BaseJsonModel baseJsonModel;
    volatile Sequence sequence;

    public LaunchEcardSocialTradeRefundReq(String orderNo, Integer refundFee, String refundOrderNo) {
        this.orderNo = orderNo;
        this.refundFee = refundFee;
        this.refundOrderNo = refundOrderNo;
        sequence = new Sequence();
        baseJsonModel = new BaseJsonModel();
        baseJsonModel.setBusinessId("0004");
        baseJsonModel.setBusinessSeq(sequence.getSequence());
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(Integer refundFee) {
        this.refundFee = refundFee;
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo;
    }

    @Override
    public String toString() {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("t1", baseJsonModel.getBusinessId());
            data.put("t2", baseJsonModel.getBusinessSeq());
            data.put("c1", this.getOrderNo());
            data.put("c2", this.getRefundFee());
            data.put("c3", this.getRefundOrderNo());
            return JsonUtils.toJSONString(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
