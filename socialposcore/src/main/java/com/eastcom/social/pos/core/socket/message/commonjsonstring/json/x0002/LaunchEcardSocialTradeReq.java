package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0002;

import com.eastcom.social.pos.core.socket.base.Sequence;
import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;
import com.eastcom.social.pos.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class LaunchEcardSocialTradeReq {
    private String orderNo; //交易预支付单号 c1
    private Integer fee;    //交易金额(分) c2
    private Integer balance;    //医保个人账户余额(分) c3
    private BaseJsonModel baseJsonModel;
    volatile Sequence sequence;

    public LaunchEcardSocialTradeReq(String orderNo, Integer fee, Integer balance) {
        this.orderNo = orderNo;
        this.fee = fee;
        this.balance = balance;
        sequence = new Sequence();
        baseJsonModel = new BaseJsonModel();
        baseJsonModel.setBusinessId("0002");
        baseJsonModel.setBusinessSeq(sequence.getSequence());
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("t1", baseJsonModel.getBusinessId());
            data.put("t2", baseJsonModel.getBusinessSeq());
            data.put("c1", this.getOrderNo());
            data.put("c2", this.getFee());
            data.put("c3", this.getBalance());
            return JsonUtils.toJSONString(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
