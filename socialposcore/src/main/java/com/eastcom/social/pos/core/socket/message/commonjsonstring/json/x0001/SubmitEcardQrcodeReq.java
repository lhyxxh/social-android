package com.eastcom.social.pos.core.socket.message.commonjsonstring.json.x0001;

import com.eastcom.social.pos.core.socket.base.Sequence;
import com.eastcom.social.pos.core.socket.message.commonjsonstring.json.BaseJsonModel;
import com.eastcom.social.pos.core.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class SubmitEcardQrcodeReq {
    private String qrcode;  //电子二维码 c1
    private Integer channelType;    //二维码渠道来源 c2
    private Integer createOrderNo;   //是否生成订单
    private BaseJsonModel baseJsonModel;
    volatile Sequence sequence;

    public SubmitEcardQrcodeReq(String qrcode, Integer channelType, Integer createOrderNo) {
        this.qrcode = qrcode;
        this.channelType = channelType;
        this.createOrderNo = createOrderNo;
        sequence = new Sequence();
        baseJsonModel = new BaseJsonModel();
        baseJsonModel.setBusinessId("0001");
        baseJsonModel.setBusinessSeq(sequence.getSequence());
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }

    public Integer getCreateOrderNo() {
        return createOrderNo;
    }

    public void setCreateOrderNo(Integer createOrderNo) {
        this.createOrderNo = createOrderNo;
    }

    @Override
    public String toString() {
        try {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("t1", baseJsonModel.getBusinessId());
            data.put("t2", baseJsonModel.getBusinessSeq());
            data.put("c1", this.getQrcode());
            data.put("c2", this.getChannelType());
            data.put("c3", this.getCreateOrderNo());
            return JsonUtils.toJSONString(data);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
