package com.eastcom.social.pos.core.orm.entity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table COMMAND_CONFIRM.
 */
public class CommandConfirm {

    private String ID;
    private Integer type;

    public CommandConfirm() {
    }

    public CommandConfirm(String ID) {
        this.ID = ID;
    }

    public CommandConfirm(String ID, Integer type) {
        this.ID = ID;
        this.type = type;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

}
