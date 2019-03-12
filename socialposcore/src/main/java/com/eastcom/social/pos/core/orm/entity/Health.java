package com.eastcom.social.pos.core.orm.entity;

import java.util.Date;

public class Health {
	private String id;
	private String bar_code;
	private String name;
	private String production_unit;
	private String production_address;
	private String specification;
	private String dosage_form;
	private int original_price;
	private int social_category;
	private String health_function;
	private String approval_no;
	private Date approval_date;
	private String notes;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBar_code() {
		return bar_code;
	}
	public void setBar_code(String bar_code) {
		this.bar_code = bar_code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProduction_unit() {
		return production_unit;
	}
	public void setProduction_unit(String production_unit) {
		this.production_unit = production_unit;
	}
	public String getProduction_address() {
		return production_address;
	}
	public void setProduction_address(String production_address) {
		this.production_address = production_address;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public String getDosage_form() {
		return dosage_form;
	}
	public void setDosage_form(String dosage_form) {
		this.dosage_form = dosage_form;
	}
	public int getOriginal_price() {
		return original_price;
	}
	public void setOriginal_price(int original_price) {
		this.original_price = original_price;
	}
	public int getSocial_category() {
		return social_category;
	}
	public void setSocial_category(int social_category) {
		this.social_category = social_category;
	}
	public String getHealth_function() {
		return health_function;
	}
	public void setHealth_function(String health_function) {
		this.health_function = health_function;
	}
	public String getApproval_no() {
		return approval_no;
	}
	public void setApproval_no(String approval_no) {
		this.approval_no = approval_no;
	}
	public Date getApproval_date() {
		return approval_date;
	}
	public void setApproval_date(Date approval_date) {
		this.approval_date = approval_date;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Health() {
		super();
	}
	public Health(String id, String bar_code, String name,
			String production_unit, String production_address,
			String specification, String dosage_form, int original_price,
			int social_category, String health_function, String approval_no,
			Date approval_date, String notes) {
		super();
		this.id = id;
		this.bar_code = bar_code;
		this.name = name;
		this.production_unit = production_unit;
		this.production_address = production_address;
		this.specification = specification;
		this.dosage_form = dosage_form;
		this.original_price = original_price;
		this.social_category = social_category;
		this.health_function = health_function;
		this.approval_no = approval_no;
		this.approval_date = approval_date;
		this.notes = notes;
	}
	
}
