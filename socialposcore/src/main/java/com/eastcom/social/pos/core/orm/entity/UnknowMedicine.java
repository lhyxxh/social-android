package com.eastcom.social.pos.core.orm.entity;

public class UnknowMedicine {

    private String barCode;

    public UnknowMedicine() {
    }

    public UnknowMedicine(String barCode) {
        this.barCode = barCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

}
