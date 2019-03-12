package com.eastcom.social.pos.listener;

import java.util.Date;
import java.util.List;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.core.service.TradeDetailService;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.listener.trade.TradeRespListener;
import com.eastcom.social.pos.core.socket.message.SoTradeType;
import com.eastcom.social.pos.core.socket.message.trade.TradeRespMessage;
import com.eastcom.social.pos.core.socket.message.tradedetail.TradeDetailMessage;
import com.eastcom.social.pos.util.MyLog;

/**
 * 上传社保交易汇总成功回复监听回调
 * 
 * @author eronc
 *
 */
public class MyTradeRespListener implements TradeRespListener {

	private TradeService tradeService;
	private TradeDetailService tradeDetailService;
	private SoClient client;

	public MyTradeRespListener() {
		tradeService = TradeService.getInstance(MyApplicationLike.getContext());
		tradeDetailService = TradeDetailService.getInstance(MyApplicationLike
				.getContext());

	}

	@Override
	public void handlerRespMessage(TradeRespMessage message) {
		client = MySoClient.newInstance().getClient();
		TradeRespMessage socialTradeRespMessage = new TradeRespMessage(message);
		int tradeRandom = socialTradeRespMessage.getTradeRandom();
		changeTrade(tradeRandom);
		uploadTradeDetail(tradeRandom);
	}

	/**
	 * 上传交易明细信息到后台
	 * 
	 * @param tradeNo
	 *            汇总流水号
	 */
	private void uploadTradeDetail(int tradeNo) {
		try {
			List<Trade> trades = tradeService.queryByTradeNo(tradeNo);
			if (trades.size() >= 0) {
				Trade trade = trades.get(0);
				String fk_trade_id = trade.getId();
				List<TradeDetail> list = tradeDetailService
						.queryByFkTradeId(fk_trade_id);
				if (list.size() >= 0) {
					for (int i = 0; i < list.size(); i++) {
						TradeDetail tradeDetail = list.get(i);
						sendTradeDetailMessage(trade, tradeDetail);
					}
				} else {
					MyLog.e("MyTradeRespListener",
							"uploadTradeDetail tradedetails size = 0");
				}
			} else {
				MyLog.e("MyTradeRespListener",
						"uploadTradeDetail trades size = 0");
			}

		} catch (Exception e) {
			MyLog.e("MyTradeRespListener", "uploadTradeDetail" + e.getMessage());
		}
	}

	/**
	 * 上传交易明细信息到后台
	 * 
	 * @param mTradeDetailsList
	 * @param mTrade
	 * @param cipherText
	 * @param date
	 */
	private void sendTradeDetailMessage(Trade mTrade, TradeDetail pi) {
		try {
			String barCode = pi.getBar_code();
			String superVisionCode = pi.getSuper_vision_code();
			int actualPrice = pi.getActual_price();
			int amount = pi.getAmount();
			int socialCategory = pi.getSocial_category();
			String rfsamNo = mTrade.getRfsam_no();
			String psamNo = mTrade.getPsam_no();
			int tradeRandom = mTrade.getTrade_no();
			Date tradeTime = mTrade.getTrade_time();
			int tradeDetailNo = pi.getDetail_trade();
			String tradeType;
			if (mTrade.getTrade_type() == Integer
					.valueOf(SoTradeType.医院交易_82字节)) {
				tradeType = SoTradeType.医院交易_82字节;
			} else if (mTrade.getTrade_type() == Integer
					.valueOf(SoTradeType.医院交易撤单_82字节)) {
				tradeType = SoTradeType.医院交易撤单_82字节;
			} else if (mTrade.getTrade_type() == Integer
					.valueOf(SoTradeType.医保交易_82字节)) {
				tradeType = SoTradeType.医保交易_82字节;
			} else if (mTrade.getTrade_type() == Integer
					.valueOf(SoTradeType.医保交易撤单_82字节)) {
				tradeType = SoTradeType.医保交易撤单_82字节;
			} else if (mTrade.getTrade_type() == Integer 
					.valueOf(SoTradeType.金融交易_82字节)) {
				tradeType = SoTradeType.金融交易_82字节;
			} else {
				tradeType = SoTradeType.金融交易撤单_82字节;
			}
			TradeDetailMessage tradeDetailMessage = new TradeDetailMessage(
					barCode, superVisionCode, actualPrice, amount,
					socialCategory, rfsamNo, psamNo, tradeRandom, tradeTime,
					tradeType, tradeDetailNo);
			client.sendMessage(tradeDetailMessage);
		} catch (Exception e) {
			MyLog.e("MyTradeRespListener",
					"sendTradeDetailMessage" + e.getMessage());
		}

	}

	/**
	 * 修改上传成功字段
	 * 
	 * @param tradeRandom
	 */
	public void changeTrade(int tradeRandom) {
		try {
			List<Trade> list = tradeService.queryByTradeNo(tradeRandom);
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Trade trade = list.get(i);
					trade.setIs_upload(0);
					tradeService.saveTrade(trade);
				}
			} else {
				MyLog.e("MyTradeRespListener", "" + tradeRandom);
			}

		} catch (Exception e) {
			MyLog.e("MyTradeRespListener", "" + e.getMessage());
		}

	}
}
