package com.eastcom.social.pos.entity;

import com.eastcom.social.pos.core.orm.entity.TradeDetail;
/**
 * 打印交易实体
 * @author Ljj
 * 上午10:34:39
 */
public class MyTradeDetail {
	private TradeDetail td;
	private Drug dr;
	public TradeDetail getTd() {
		return td;
	}
	public void setTd(TradeDetail td) {
		this.td = td;
	}
	public Drug getDr() {
		return dr;
	}
	public void setDr(Drug dr) {
		this.dr = dr;
	}
	
}
