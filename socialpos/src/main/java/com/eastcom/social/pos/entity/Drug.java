package com.eastcom.social.pos.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eastcom.social.pos.core.orm.entity.Health;
import com.eastcom.social.pos.core.orm.entity.Medicine;

/**
 * 界面药品信息实体
 * 
 * @author Ljj 上午10:06:38
 */
public class Drug implements Serializable{
	private String id;
	private String bar_code;
	private String name;
	private String production_unit;
	private String production_address;
	private String specification;
	private String dosage_form;
	private int original_price;
	private int social_category;
	private String approval_no;
	private Date approval_date;
	private String notes;
	private String super_code;

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

	
	
	public String getSuper_code() {
		return super_code;
	}

	public void setSuper_code(String super_code) {
		this.super_code = super_code;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Drug(String id, String bar_code, String name,
			String production_unit, String production_address,
			String specification, String dosage_form, int original_price,
			int social_category, String approval_no, Date approval_date,
			String notes) {
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
		this.approval_no = approval_no;
		this.approval_date = approval_date;
		this.notes = notes;
	}

	public Drug() {

	}
	/**
	 * 药品实体转换为drug
	 * @param list
	 * @param result
	 */
	public void MedicineToDrug(List<Medicine> list, ArrayList<Drug> result) {
		for (int i = 0; i < list.size(); i++) {
			Medicine pi = list.get(i);
			Drug item = new Drug();
			item.bar_code = pi.getBar_code();
			item.name = pi.getBar_code();
			item.production_unit = pi.getProduction_unit();
			item.production_address = pi.getProduction_address();
			item.specification = pi.getSpecification();
			item.dosage_form = pi.getDosage_form();
			item.original_price = pi.getOriginal_price();
			item.social_category = pi.getSocial_category();
			item.approval_no = pi.getApproval_no();
			item.approval_date = pi.getApproval_date();
			item.notes = pi.getNotes();
			result.add(item);
		}
	}
	/**
	 * 保健品实体转换为drug
	 * @param list
	 * @param result
	 */
	public void HealthToDrug(List<Health> list, ArrayList<Drug> result) {
		for (int i = 0; i < list.size(); i++) {
			Health pi = list.get(i);
			Drug item = new Drug();
			item.bar_code = pi.getBar_code();
			item.name = pi.getBar_code();
			item.production_unit = pi.getProduction_unit();
			item.production_address = pi.getProduction_address();
			item.specification = pi.getSpecification();
			item.dosage_form = pi.getDosage_form();
			item.original_price = pi.getOriginal_price();
			item.social_category = pi.getSocial_category();
			item.approval_no = pi.getApproval_no();
			item.approval_date = pi.getApproval_date();
			item.notes = pi.getNotes();
			result.add(item);
		}
	}

}
