package com.eastcom.social.pos.core.socket.message.medicineproduct;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

import android.annotation.SuppressLint;

public class MedicineProductMessage extends SoMessage {

	private String barCode;

	private String name;

	private String engName;

	private String tradeName;

	private String productionUnit;

	private String productionAddress;

	private String specification;

	private String dosageForm;

	private int originalPrice;

	private int recipeCategory;

	private int socialCategory;

	private String approvalNo;

	private Date approvalDate;

	private String notes;

	@SuppressLint("SimpleDateFormat")
	public MedicineProductMessage(String barCode, String name, String engName, String tradeName, String productionUnit,
			String productionAddress, String specification, String dosageForm, int originalPrice, int recipeCategory,
			int socialCategory, String approvalNo, Date approvalDate, String notes) {

		short bodyLength = (short) (2 + barCode.length() + 2 + name.getBytes(CHARSET_GB).length + 2 + engName.length()
				+ 2 + tradeName.getBytes(CHARSET_GB).length + 2 + productionUnit.getBytes(CHARSET_GB).length + 2
				+ productionAddress.getBytes(CHARSET_GB).length + 2 + specification.getBytes(CHARSET_GB).length + 2
				+ dosageForm.getBytes(CHARSET_GB).length + 4 + 1 + 1 + 9 + 4 + 2 + notes.getBytes(CHARSET_GB).length);

		this.barCode = barCode;
		this.name = name;
		this.engName = engName;
		this.tradeName = tradeName;
		this.productionUnit = productionUnit;
		this.productionAddress = productionAddress;
		this.specification = specification;
		this.dosageForm = dosageForm;
		this.originalPrice = originalPrice;
		this.recipeCategory = recipeCategory;
		this.socialCategory = socialCategory;
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
		System.arraycopy(ByteUtils.intToBytes2(engName.length()), 0, data, position, 2);
		position += 2;
		System.arraycopy(engName.getBytes(CHARSET), 0, data, position, engName.length());
		position += engName.length();
		System.arraycopy(ByteUtils.intToBytes2(tradeName.getBytes(CHARSET_GB).length), 0, data, position, 2);
		position += 2;
		System.arraycopy(tradeName.getBytes(CHARSET_GB), 0, data, position, tradeName.getBytes(CHARSET_GB).length);
		position += tradeName.getBytes(CHARSET_GB).length;
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
		data[position] = (byte) recipeCategory;
		position += 1;
		data[position] = (byte) socialCategory;
		position += 1;
		System.arraycopy(approvalNo.getBytes(CHARSET), 0, data, position, 9);
		position += 9;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		System.arraycopy(ByteUtils.hexStr2bytes(sdf.format(approvalDate)), 0, data, position, 4);
		position += 4;
		System.arraycopy(ByteUtils.intToBytes2(notes.getBytes(CHARSET_GB).length), 0, data, position, 2);
		position += 2;
		System.arraycopy(notes.getBytes(CHARSET_GB), 0, data, position, notes.getBytes(CHARSET_GB).length);
		position += notes.getBytes(CHARSET_GB).length;

		this.command = SoCommand.注册药品信息;
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

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
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

	public int getRecipeCategory() {
		return recipeCategory;
	}

	public void setRecipeCategory(int recipeCategory) {
		this.recipeCategory = recipeCategory;
	}

	public int getSocialCategory() {
		return socialCategory;
	}

	public void setSocialCategory(int socialCategory) {
		this.socialCategory = socialCategory;
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
