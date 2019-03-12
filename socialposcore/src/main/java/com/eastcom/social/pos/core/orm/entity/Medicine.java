package com.eastcom.social.pos.core.orm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.eastcom.social.pos.core.socket.message.querymedicine.QueryMedicineRespMessage;

public class Medicine implements Serializable{
	private String id;
	private String bar_code;
	private String name;
	private String eng_name;
	private String trade_name;
	private String production_unit;
	private String production_address;
	private String specification;
	private String dosage_form;
	private int original_price;
	private int recipe_category;
	private int social_category;
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
	public String getEng_name() {
		return eng_name;
	}
	public void setEng_name(String eng_name) {
		this.eng_name = eng_name;
	}
	public String getTrade_name() {
		return trade_name;
	}
	public void setTrade_name(String trade_name) {
		this.trade_name = trade_name;
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
	public int getRecipe_category() {
		return recipe_category;
	}
	public void setRecipe_category(int recipe_category) {
		this.recipe_category = recipe_category;
	}
	public int getSocial_category() {
		return social_category;
	}
	public void setSocial_category(int social_category) {
		this.social_category = social_category;
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
	
	public Medicine() {
		super();
	}
	public Medicine(String id, String bar_code, String name, String eng_name,
			String trade_name, String production_unit,
			String production_address, String specification,
			String dosage_form, int original_price, int recipe_category,
			int social_category, String approval_no, Date approval_date,
			String notes) {
		super();
		this.id = id;
		this.bar_code = bar_code;
		this.name = name;
		this.eng_name = eng_name;
		this.trade_name = trade_name;
		this.production_unit = production_unit;
		this.production_address = production_address;
		this.specification = specification;
		this.dosage_form = dosage_form;
		this.original_price = original_price;
		this.recipe_category = recipe_category;
		this.social_category = social_category;
		this.approval_no = approval_no;
		this.approval_date = approval_date;
		this.notes = notes;
	}
	
	public void setEntity(Medicine result, QueryMedicineRespMessage pi) {
		result.setId(UUID.randomUUID().toString());
		result.setBar_code(pi.getBarCode());
		result.setName(pi.getName());
		result.setEng_name(pi.getEngName());
		result.setTrade_name(pi.getTradeName());
		result.setProduction_unit(pi.getProductionUnit());
		result.setProduction_address(pi.getProductionAddress());
		result.setSpecification(pi.getSpecification());
		result.setDosage_form(pi.getDosageForm());
		result.setOriginal_price(pi.getOriginalPrice());
		result.setRecipe_category(pi.getRecipeCategory());
		result.setSocial_category(pi.getSocialCategory());
		result.setApproval_no(pi.getApprovalNo());
		result.setApproval_date(pi.getApprovalDate());
		result.setNotes(pi.getNotes());
		
		
	}
	
}
