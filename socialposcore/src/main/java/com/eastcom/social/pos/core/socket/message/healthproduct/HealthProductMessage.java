package com.eastcom.social.pos.core.socket.message.healthproduct;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

import android.annotation.SuppressLint;

public class HealthProductMessage extends SoMessage {

	private String barCode;

	private String name;

	private String productionUnit;

	private String productionAddress;

	private String specification;

	private String dosageForm;

	private int originalPrice;

	private int socialCategory;

	private String healthFunction;

	private String approvalNo;

	private Date approvalDate;

	private String notes;

	@SuppressLint("SimpleDateFormat")
	public HealthProductMessage(String barCode, String name, String productionUnit, String productionAddress,
			String specification, String dosageForm, int originalPrice, int socialCategory, String healthFunction,
			String approvalNo, Date approvalDate, String notes) {
		short bodyLength = (short) (2 + barCode.length() + 2 + name.getBytes(CHARSET_GB).length + 2
				+ productionUnit.getBytes(CHARSET_GB).length + 2 + productionAddress.getBytes(CHARSET_GB).length + 2
				+ specification.getBytes(CHARSET_GB).length + 2 + dosageForm.getBytes(CHARSET_GB).length + 4 + 1 + 2
				+ healthFunction.getBytes(CHARSET_GB).length + 9 + 4 + 2 + notes.getBytes(CHARSET_GB).length);

		this.barCode = barCode;
		this.name = name;
		this.productionUnit = productionUnit;
		this.productionAddress = productionAddress;
		this.specification = specification;
		this.dosageForm = dosageForm;
		this.originalPrice = originalPrice;
		this.socialCategory = socialCategory;
		this.healthFunction = healthFunction;
		this.approvalNo = approvalNo;
		this.approvalDate = approvalDate;
		this.notes = notes;

		byte[] data = new byte[bodyLength];
		int position = 0;
		System.arraycopy(ByteUtils.intToBytes2(barCode.length()), 0, data, position, 2);
		position += 2;
		System.arraycopy(barCode.getBytes(CHARSET), 0, data, position, barCode.length());
		position += barCode.length();
		System.arraycopy(ByteUtils.intToBytes2(name.getBytes(CHARSET_GB).length), 0, data, position, 2);
		position += 2;
		System.arraycopy(name.getBytes(CHARSET_GB), 0, data, position, name.getBytes(CHARSET_GB).length);
		position += name.getBytes(CHARSET_GB).length;
		System.arraycopy(ByteUtils.intToBytes2(productionUnit.getBytes(CHARSET_GB).length), 0, data, position, 2);
		position += 2;
		System.arraycopy(productionUnit.getBytes(CHARSET_GB), 0, data, position,
				productionUnit.getBytes(CHARSET_GB).length);
		position += productionUnit.getBytes(CHARSET_GB).length;
		System.arraycopy(ByteUtils.intToBytes2(productionAddress.getBytes(CHARSET_GB).length), 0, data, position, 2);
		position += 2;
		System.arraycopy(productionAddress.getBytes(CHARSET_GB), 0, data, position,
				productionAddress.getBytes(CHARSET_GB).length);
		position += productionAddress.getBytes(CHARSET_GB).length;
		System.arraycopy(ByteUtils.intToBytes2(specification.getBytes(CHARSET_GB).length), 0, data, position, 2);
		position += 2;
		System.arraycopy(specification.getBytes(CHARSET_GB), 0, data, position,
				specification.getBytes(CHARSET_GB).length);
		position += specification.getBytes(CHARSET_GB).length;
		System.arraycopy(ByteUtils.intToBytes2(dosageForm.getBytes(CHARSET_GB).length), 0, data, position, 2);
		position += 2;
		System.arraycopy(dosageForm.getBytes(CHARSET_GB), 0, data, position, dosageForm.getBytes(CHARSET_GB).length);
		position += dosageForm.getBytes(CHARSET_GB).length;
		System.arraycopy(ByteUtils.intToBytes(originalPrice), 0, data, position, 4);
		position += 4;
		data[position] = (byte) socialCategory;
		position += 1;
		System.arraycopy(ByteUtils.intToBytes2(healthFunction.getBytes(CHARSET_GB).length), 0, data, position, 2);
		position += 2;
		System.arraycopy(healthFunction.getBytes(CHARSET_GB), 0, data, position,
				healthFunction.getBytes(CHARSET_GB).length);
		position += healthFunction.getBytes(CHARSET_GB).length;
		System.arraycopy(approvalNo.getBytes(CHARSET), 0, data, position, 9);
		position += 9;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		System.arraycopy(ByteUtils.hexStr2bytes(sdf.format(approvalDate)), 0, data, position, 4);
		position += 4;
		System.arraycopy(ByteUtils.intToBytes2(notes.getBytes(CHARSET_GB).length), 0, data, position, 2);
		position += 2;
		System.arraycopy(notes.getBytes(CHARSET_GB), 0, data, position, notes.getBytes(CHARSET_GB).length);
		position += notes.getBytes(CHARSET_GB).length;

		this.command = SoCommand.注册保健品信息;
		this.total = bodyLength;
		this.body = new byte[bodyLength];
		System.arraycopy(data, 0, this.body, 0, bodyLength);

		super.computeChecksum();

	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductionUnit() {
		return productionUnit;
	}

	public void setProductionUnit(String productionUnit) {
		this.productionUnit = productionUnit;
	}

	public String getProductionAddress() {
		return productionAddress;
	}

	public void setProductionAddress(String productionAddress) {
		this.productionAddress = productionAddress;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getDosageForm() {
		return dosageForm;
	}

	public void setDosageForm(String dosageForm) {
		this.dosageForm = dosageForm;
	}

	public int getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(int originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getSocialCategory() {
		return socialCategory;
	}

	public void setSocialCategory(int socialCategory) {
		this.socialCategory = socialCategory;
	}

	public String getHealthFunction() {
		return healthFunction;
	}

	public void setHealthFunction(String healthFunction) {
		this.healthFunction = healthFunction;
	}

	public String getApprovalNo() {
		return approvalNo;
	}

	public void setApprovalNo(String approvalNo) {
		this.approvalNo = approvalNo;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
