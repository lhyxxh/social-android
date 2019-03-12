package com.eastcom.social.pos.listener;

import java.util.List;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.core.service.TradeDetailService;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.core.socket.listener.tradedetail.TradeDetailRespListener;
import com.eastcom.social.pos.core.socket.message.tradedetail.TradeDetailRespMessage;
import com.eastcom.social.pos.util.MyLog;

/**
 * 上传交易明细成功回复监听回调
 * 
 * @author eronc
 *
 */
public class MyTradeDetailRespListener implements TradeDetailRespListener {

	private TradeService tradeService;
	private TradeDetailService tradeDetailService;

	public MyTradeDetailRespListener() {
		tradeService = TradeService.getInstance(MyApplicationLike.getContext());
		tradeDetailService = TradeDetailService.getInstance(MyApplicationLike
				.getContext());
	}

	@Override
	public void handlerRespMessage(TradeDetailRespMessage message) {
		TradeDetailRespMessage tradeDetailRespMessage = new TradeDetailRespMessage(
				message);

		int saveSuccess = tradeDetailRespMessage.getSaveSuccess();
		if (saveSuccess == 0) {
			int tradeRandom = tradeDetailRespMessage.getTradeRandom();
			int exist = tradeDetailRespMessage.getExist();
			int tradeDetailNo = tradeDetailRespMessage.getTradeDetailNo();
			changeTradeDetail(tradeRandom, tradeDetailNo, exist, saveSuccess);
		}

	}

	/**
	 * 修改明细上传成功字段
	 * 
	 * @param tradeRandom
	 * @param tradeDetailNo
	 * @param exist
	 * @param saveSuccess
	 */
	public void changeTradeDetail(int tradeRandom, int tradeDetailNo,
			int exist, int saveSuccess) {
		try {
			List<Trade> listTrade = tradeService.queryByTradeNo(tradeRandom);
			if (listTrade.size() > 0) {
				for (int i = 0; i < listTrade.size(); i++) {
					Trade trade = listTrade.get(i);
					List<TradeDetail> list = tradeDetailService
							.queryByFkTradeId(trade.getId());
					for (int j = 0; j < list.size(); j++) {
						TradeDetail tradeDetail = list.get(j);
						tradeDetail.setIs_upload(0);
						tradeDetailService.saveTradeDetail(tradeDetail);
					}
				}
			}else {
				MyLog.e("MyTradeDetailRespListener", "uploadTrade trades size = 0");
			}
		} catch (Exception e) {
			MyLog.e("MyTradeDetailRespListener", "" + e.getMessage());
		}

	}
}
