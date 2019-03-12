package com.eastcom.social.pos.core.orm.entity;

import java.util.Date;

public class Trade {
	private String id;
	private int trade_no;
	private int pre_trade_no;
	private String sign_board_no;
	private String rfsam_no;
	private String psam_no;
	private String terminal_code;
	private Date trade_time;
	private String ref_no;
	private String social_card_no;
	private String id_card_no;
	private int trade_type;
	private int trade_state;
	private int trade_money;
	private int ss_pay;
	private int individual_pay;
	private int is_upload;
	private int amount;
	private int is_revoke;
	private String trace;
	private int send_count;
	private String bank_card_no;
	private String encrypt;

	public int getSend_count() {
		return send_count;
	}

	public void setSend_count(int send_count) {
		this.send_count = send_count;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(int trade_no) {
		this.trade_no = trade_no;
	}

	public int getPre_trade_no() {
		return pre_trade_no;
	}

	public void setPre_trade_no(int pre_trade_no) {
		this.pre_trade_no = pre_trade_no;
	}

	public String getSign_board_no() {
		return sign_board_no;
	}

	public void setSign_board_no(String sign_board_no) {
		this.sign_board_no = sign_board_no;
	}

	public String getRfsam_no() {
		return rfsam_no;
	}

	public void setRfsam_no(String rfsam_no) {
		this.rfsam_no = rfsam_no;
	}

	public String getPsam_no() {
		return psam_no;
	}

	public void setPsam_no(String psam_no) {
		this.psam_no = psam_no;
	}

	public String getTerminal_code() {
		return terminal_code;
	}

	public void setTerminal_code(String terminal_code) {
		this.terminal_code = terminal_code;
	}

	public Date getTrade_time() {
		return trade_time;
	}

	public void setTrade_time(Date trade_time) {
		this.trade_time = trade_time;
	}

	public String getRef_no() {
		return ref_no;
	}

	public void setRef_no(String ref_no) {
		this.ref_no = ref_no;
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

	public int getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(int trade_type) {
		this.trade_type = trade_type;
	}

	public int getTrade_state() {
		return trade_state;
	}

	public void setTrade_state(int trade_state) {
		this.trade_state = trade_state;
	}

	public int getTrade_money() {
		return trade_money;
	}

	public void setTrade_money(int trade_money) {
		this.trade_money = trade_money;
	}

	public int getSs_pay() {
		return ss_pay;
	}

	public void setSs_pay(int ss_pay) {
		this.ss_pay = ss_pay;
	}

	public int getIndividual_pay() {
		return individual_pay;
	}

	public void setIndividual_pay(int individual_pay) {
		this.individual_pay = individual_pay;
	}

	public int getIs_upload() {
		return is_upload;
	}

	public void setIs_upload(int is_upload) {
		this.is_upload = is_upload;
	}

	public int getIs_revoke() {
		return is_revoke;
	}

	public void setIs_revoke(int is_revoke) {
		this.is_revoke = is_revoke;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(String trace) {
		this.trace = trace;
	}

	public Trade(String id) {
		this.id = id;
	}

	public String getBank_card_no() {
		return bank_card_no;
	}

	public void setBank_card_no(String bank_card_no) {
		this.bank_card_no = bank_card_no;
	}

	public String getEncrypt() {
		return encrypt;
	}

	public void setEncrypt(String encrypt) {
		this.encrypt = encrypt;
	}

	public Trade() {
	}

	public Trade(String id, int trade_no, int pre_trade_no,
			String sign_board_no, String rfsam_no, String psam_no,
			String terminal_code, Date trade_time, String ref_no,
			String social_card_no, String id_card_no, int trade_type,
			int trade_state, int trade_money, int ss_pay, int individual_pay,
			int is_upload, int amount, int is_revoke, String trace,
			int send_count, String bank_card_no, String encrypt) {
		super();
		this.id = id;
		this.trade_no = trade_no;
		this.pre_trade_no = pre_trade_no;
		this.sign_board_no = sign_board_no;
		this.rfsam_no = rfsam_no;
		this.psam_no = psam_no;
		this.terminal_code = terminal_code;
		this.trade_time = trade_time;
		this.ref_no = ref_no;
		this.social_card_no = social_card_no;
		this.id_card_no = id_card_no;
		this.trade_type = trade_type;
		this.trade_state = trade_state;
		this.trade_money = trade_money;
		this.ss_pay = ss_pay;
		this.individual_pay = individual_pay;
		this.is_upload = is_upload;
		this.amount = amount;
		this.is_revoke = is_revoke;
		this.trace = trace;
		this.send_count = send_count;
		this.bank_card_no = bank_card_no;
		this.encrypt = encrypt;
	}


}
