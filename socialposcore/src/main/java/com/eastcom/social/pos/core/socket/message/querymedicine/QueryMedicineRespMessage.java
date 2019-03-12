package com.eastcom.social.pos.core.socket.message.querymedicine;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.utils.ByteUtils;

import android.annotation.SuppressLint;

public class QueryMedicineRespMessage extends SoMessage {

	private boolean exist;

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
	public QueryMedicineRespMessage(SoMessage message) throws Exception {
		super(message);
		setExist(false);
		byte[] keyData = message.getBody();

		// 产品条形码
		int position = 1;
		int barCodeLen = ByteUtils.bytes2Int(keyData, position, 2);
		position += 2;
		String barCode = new String(keyData, position, barCodeLen, CHARSET);
		position += barCodeLen;
		setBarCode(barCode);

		if (keyData[0] == 0x00) {
			setExist(true);
			// 产品名称
			int nameLen = ByteUtils.bytes2Int(keyData, position, 2);
			position += 2;
			String name = new String(keyData, position, nameLen, CHARSET_GB);
			position += nameLen;
			// 产品英文名称
			int engNameLen = ByteUtils.bytes2Int(keyData, position, 2);
			position += 2;
			String engName = new String(keyData, position, engNameLen, CHARSET);
			position += engNameLen;
			// 商品名
			int tradeNameLen = ByteUtils.bytes2Int(keyData, position, 2);
			position += 2;
			String tradeName = new String(keyData, position, tradeNameLen, CHARSET_GB);
			position += tradeNameLen;
			// 生产单位
			int productionUnitLen = ByteUtils.bytes2Int(keyData, position, 2);
			position += 2;
			String productionUnit = new String(keyData, position, productionUnitLen, CHARSET_GB);
			position += productionUnitLen;
			// 生产地址
			int productionAddressLen = ByteUtils.bytes2Int(keyData, position, 2);
			position += 2;
			String productionAddress = new String(keyData, position, productionAddressLen, CHARSET_GB);
			position += productionAddressLen;
			// 规格
			int specificationLen = ByteUtils.bytes2Int(keyData, position, 2);
			position += 2;
			String specification = new String(keyData, position, specificationLen, CHARSET_GB);
			position += specificationLen;
			// 剂型
			int dosageFormLen = ByteUtils.bytes2Int(keyData, position, 2);
			position += 2;
			String dosageForm = new String(keyData, position, dosageFormLen, CHARSET_GB);
			position += dosageFormLen;
			// 原价
			int originalPrice = ByteUtils.bytes2Int(keyData, position, 4);
			position += 4;
			// 处方类型
			int recipeCategory = ByteUtils.bytes2Int(keyData, position, 1);
			position += 1;
			// 医保类型
			int socialCategory = ByteUtils.bytes2Int(keyData, position, 1);
			position += 1;
			// 批准文号
			String approvalNo = "国药准字" + new String(keyData, position, 9, CHARSET);
			position += 9;
			// 批准日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date approvalDate = sdf.parse(ByteUtils.bcd2Str(keyData, position, 4) + "000000");
			position += 4;
			// 备注信息
			int notesLen = ByteUtils.bytes2Int(keyData, position, 2);
			position += 2;
			String notes = new String(keyData, position, notesLen, CHARSET_GB);

			setName(name);
			setEngName(engName);
			setTradeName(tradeName);
			setProductionUnit(productionUnit);
			setProductionAddress(productionAddress);
			setSpecification(specification);
			setDosageForm(dosageForm);
			setOriginalPrice(originalPrice);
			setRecipeCategory(recipeCategory);
			setSocialCategory(socialCategory);
			setApprovalNo(approvalNo);
			setApprovalDate(approvalDate);
			setNotes(notes);
		}
	}

	public boolean isExist() {
		return exist;
	}

	public void setExist(boolean exist) {
		this.exist = exist;
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
