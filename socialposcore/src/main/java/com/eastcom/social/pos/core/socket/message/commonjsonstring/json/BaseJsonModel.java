package com.eastcom.social.pos.core.socket.message.commonjsonstring.json;

public class BaseJsonModel {
    String businessId;   //t1
    int businessSeq;   //t2

    public BaseJsonModel() {
    }

    public BaseJsonModel(String businessId, int businessSeq) {
        this.businessId = businessId;
        this.businessSeq = businessSeq;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public int getBusinessSeq() {
        return businessSeq;
    }

    public void setBusinessSeq(int businessSeq) {
        this.businessSeq = businessSeq;
    }

    public static String toHexByteString(byte... data) {
        StringBuilder sb = new StringBuilder();
        if (data != null && data.length > 0) {
            for (int i = 0; i < data.length; i++) {
                if (i == 0) {

                }
                else if (i == 14) {
                    sb.append("\r\n");
                } else {
                    sb.append(" ");
                }

                String hv = Integer.toHexString(data[i] & 0xFF).toUpperCase();
                sb.append(hv.length() < 2 ? "0x0" : "0x").append(hv);
            }
        }

        return sb.toString();
    }
}
