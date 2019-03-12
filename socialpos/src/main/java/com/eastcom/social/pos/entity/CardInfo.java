package com.eastcom.social.pos.entity;

import java.io.Serializable;



/**
 * 界面药品信息实体
 * 
 * @author Ljj 上午10:06:38
 */
public class CardInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String bank_no;
	private String social_card_no;
	private String id_card_no;
	private String name;
	private String id_code;
	private String sex;
	private String startDate;
	private String endDtae;
	
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
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDtae() {
		return endDtae;
	}
	public void setEndDtae(String endDtae) {
		this.endDtae = endDtae;
	}

	public static void setCardInfo(String string, CardInfo cardInfo) {
		String social_card_no = string.split("~nsbkkh~t")[1].split("~n")[0];// 社保卡号
		String id_card_no = string.split("~nsfzhm~t")[1].split("~n")[0];// 身份证号
		String name = string.split("~nxm~t")[1].split("~n")[0];// 姓名 ~nxm~t
		String id_code = string.split("~nsbm~t")[1].split("~n")[0];// 识别码
		int sexInt = Integer.valueOf(id_card_no.substring(16, 17));
		String sex = sexInt % 2 == 0 ? "女" : "男";
		String startDate = string.split("~nfkrq~t")[1].split("~n")[0];// 发卡日期
		String endDtae = string.split("~nyxq~t")[1].split("~n")[0];// 有效期
		String bank_no = string.split("~nyhzh~t")[1].split("~n")[0];// 银行账号
		
		// ~n
//		cardInfo.setBank_no(bank_no);
		cardInfo.setBank_no("621721000000");
		cardInfo.setSocial_card_no(social_card_no);
		cardInfo.setId_card_no(id_card_no);
		cardInfo.setName(name);
		cardInfo.setId_code(id_code);
		cardInfo.setSex(sex);
		cardInfo.setStartDate(startDate);
		cardInfo.setEndDtae(endDtae);
	}


}
