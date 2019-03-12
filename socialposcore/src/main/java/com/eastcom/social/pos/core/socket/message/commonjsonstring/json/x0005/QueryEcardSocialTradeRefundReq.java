package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0005;

import com.eastcom.social.pos.core.socket.base.Sequence;
import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;
import com.eastcom.social.pos.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class QueryEcardSocialTradeRefundReq {
    private String orderNo; //交易预支付单号 c1
    private String refundOrderNo; //退款单号 c2
    private BaseJsonModel baseJsonModel;
    volatile Sequence sequence;

    public QueryEcardSocialTradeRefundReq(String orderNo, String refundOrderNo) {
        this.orderNo = orderNo;
        this.refundOrderNo = refundOrderNo;
        sequence = new Sequence();
        baseJsonModel = new BaseJsonModel();
        baseJsonModel.setBusinessId("0005");
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

    @Override
    public String toString() {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("t1", baseJsonModel.getBusinessId());
            data.put("t2", baseJsonModel.getBusinessSeq());
            data.put("c1", this.getOrderNo());
            data.put("c2", this.getRefundOrderNo());
            return JsonUtils.toJSONString(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
