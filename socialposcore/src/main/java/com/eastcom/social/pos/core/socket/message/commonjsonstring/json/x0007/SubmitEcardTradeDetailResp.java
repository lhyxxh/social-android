package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0007;

import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;

import java.util.Map;

public class SubmitEcardTradeDetailResp {

    private int status;            //保存状态     c1
    private String orderNo;       //交易预支付单号   c2
    private String orderDetailNo;    //交易明细序号   c3

    private BaseJsonModel baseJsonModel;

    public SubmitEcardTradeDetailResp(Map<String, Object> data){
        baseJsonModel = new BaseJsonModel();
        baseJsonModel.setBusinessSeq((Integer) data.get("t2"));
        baseJsonModel.setBusinessId((String)data.get("t1"));
        this.setStatus((Integer) data.get("c1"));
        this.setOrderNo((String) data.get("c2"));
        this.setOrderDetailNo((String) data.get("c3"));
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void setOrderDetailNo(String orderDetailNo) {
        this.orderDetailNo = orderDetailNo;
    }

    public int getStatus() {
        return status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getOrderDetailNo() {
        return orderDetailNo;
    }
}
