package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0002;

import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;

import java.util.Map;

public class LaunchEcardSocialTradeResp {
    private String orderNo; //交易预支付单号 c1
    private Integer status; //发起状态 c2

    private BaseJsonModel baseJsonModel;

    public LaunchEcardSocialTradeResp(Map<String, Object> data){
            baseJsonModel = new BaseJsonModel();
            baseJsonModel.setBusinessSeq((Integer) data.get("t2"));
            baseJsonModel.setBusinessId((String)data.get("t1"));
            this.setOrderNo((String) data.get("c1"));
            this.setStatus((Integer) data.get("c2"));
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
}
