package com.eastcom.social.pos.core.orm.entity;

public class BlackList {

    private String SocialCardNo;
    private Integer Status;

    public BlackList() {
    }

    public BlackList(String SocialCardNo) {
        this.SocialCardNo = SocialCardNo;
    }

    public BlackList(String SocialCardNo, Integer Status) {
        this.SocialCardNo = SocialCardNo;
        this.Status = Status;
    }

    public String getSocialCardNo() {
        return SocialCardNo;
    }

    public void setSocialCardNo(String SocialCardNo) {
        this.SocialCardNo = SocialCardNo;
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer Status) {
        this.Status = Status;
    }

}
