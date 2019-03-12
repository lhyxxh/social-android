package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0007;

import com.eastcom.social.pos.core.socket.base.Sequence;
import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;
import com.eastcom.social.pos.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class SubmitEcardTradeDetailReq {
    private String orderNo;      //交易预支付单号 c1
    private String orderDetailNo;  //交易明细序号 c2
    private String barCode;         //条形码      c3
    private String superVisionCode;    //电子监管码   c4
    private int actualPrice;        //单价        c5
    private int amount;           //数量          c6

    private BaseJsonModel baseJsonModel;
    volatile Sequence sequence;

    public SubmitEcardTradeDetailReq(String orderNo, String orderDetailNo, String barCode, String superVisionCode, int actualPrice, int amount) {
        this.orderNo = orderNo;
        this.orderDetailNo = orderDetailNo;
        this.barCode = barCode;
        this.superVisionCode = superVisionCode;
        this.actualPrice = actualPrice;
        this.amount = amount;
        sequence = new Sequence();
        baseJsonModel = new BaseJsonModel();
        baseJsonModel.setBusinessId("0007");
        baseJsonModel.setBusinessSeq(sequence.getSequence());
    }

    @Override
    public String toString() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("t1", baseJsonModel.getBusinessId());
        data.put("t2", baseJsonModel.getBusinessSeq());
        data.put("c1", this.getOrderNo());
        data.put("c2", this.getOrderDetailNo());
        data.put("c3", this.getBarCode());
        data.put("c4", this.getSuperVisionCode());
        data.put("c5", this.getActualPrice());
        data.put("c6", this.getAmount());
        return JsonUtils.toJSONString(data);
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getOrderDetailNo() {
        return orderDetailNo;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getSuperVisionCode() {
        return superVisionCode;
    }

    public int getActualPrice() {
        return actualPrice;
    }

    public int getAmount() {
        return amount;
    }
}
