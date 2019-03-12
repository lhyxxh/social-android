package com.eastcom.social.pos.core.orm.entity;

import java.util.Date;


public class CardInfo {
	
	private String id;
	private String bank_no;
	private String social_card_no;
	private String id_card_no;
	private String name;
	private String id_code;
	private String sex;
	private Date startDate;
	private Date endDtae;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBank_no() {
		return bank_no;
	}
	public void setBank_no(String bank_no) {
		this.bank_no = bank_no;
	}
	public String getSocial_card_no() {
		return social_card_no;
	}
	public void setSocial_card_no(String social_card_no) {
		this.social_card_no = social_card_no;
	}
	public String getId_card_no() {
		return id_card_no;
	}
	public void setId_card_no(String id_card_no) {
		this.id_card_no = id_card_no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId_code() {
		return id_code;
	}
	public void setId_code(String id_code) {
		this.id_code = id_code;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDtae() {
		return endDtae;
	}
	public void setEndDtae(Date endDtae) {
		this.endDtae = endDtae;
	}
	public CardInfo(String id, String bank_no, String social_card_no,
			String id_card_no, String name, String id_code, String sex,
			Date startDate, Date endDtae) {
		super();
		this.id = id;
		this.bank_no = bank_no;
		this.social_card_no = social_card_no;
		this.id_card_no = id_card_no;
		this.name = name;
		this.id_code = id_code;
		this.sex = sex;
		this.startDate = startDate;
		this.endDtae = endDtae;
	}
	public CardInfo() {
	}
	
}
