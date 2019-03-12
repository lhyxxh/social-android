package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0003;

import com.eastcom.social.pos.core.socket.base.Sequence;
import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;
import com.eastcom.social.pos.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class QueryEcardSocialTradeReq {
    private String orderNo; //交易预支付单号 c1
    private BaseJsonModel baseJsonModel;
    volatile Sequence sequence;

    public QueryEcardSocialTradeReq(String orderNo) {
        this.orderNo = orderNo;
        sequence = new Sequence();
        baseJsonModel = new BaseJsonModel();
        baseJsonModel.setBusinessId("0003");
        baseJsonModel.setBusinessSeq(sequence.getSequence());
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("t1", baseJsonModel.getBusinessId());
            data.put("t2", baseJsonModel.getBusinessSeq());
            data.put("c1", this.getOrderNo());
            return JsonUtils.toJSONString(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
