package com.eastcom.social.pos.listener;

import java.util.List;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.core.service.TradeDetailService;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.core.socket.listener.MessageRespListener;
import com.eastcom.social.pos.core.socket.message.SoCommand;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.trade.TradeRespMessage;
import com.eastcom.social.pos.core.socket.message.tradedetail.TradeDetailRespMessage;

/**
 * 上传成功回复监听回调
 * 
 * @author eronc
 *
 */
public class MyMessageRespListener implements MessageRespListener {
	private TradeService tradeService;
	private TradeDetailService tradeDetailService;

	public MyMessageRespListener() {
		tradeService = TradeService.getInstance(MyApplicationLike.getContext());
		tradeDetailService = TradeDetailService.getInstance(MyApplicationLike
				.getContext());
	}

	@Override
	public void handlerRespMessage(SoMessage message) {
		if (message.getCommand() == SoCommand.回应上传交易) {
			TradeRespMessage socialTradeRespMessage = new TradeRespMessage(
					message);
			int tradeRandom = socialTradeRespMessage.getTradeRandom();
			changeTrade(tradeRandom);
		} else if (message.getCommand() == SoCommand.回应上传交易明细) {

			TradeDetailRespMessage tradeDetailRespMessage = new TradeDetailRespMessage(
					message);
			int tradeRandom = tradeDetailRespMessage.getTradeRandom();

			int exist = tradeDetailRespMessage.getExist();

			int saveSuccess = tradeDetailRespMessage.getSaveSuccess();

			int tradeDetailNo = tradeDetailRespMessage.getTradeDetailNo();
			changeTradeDetail(tradeRandom, tradeDetailNo, exist, saveSuccess);

		} 

	}

	public void changeTrade(int tradeRandom) {
		List<Trade> list = tradeService.queryByTradeNo(tradeRandom);
		if (list.size() > 0) {
			Trade trade = list.get(0);
			trade.setIs_upload(0);
			tradeService.saveTrade(trade);
		}

	}

	public void changeTradeDetail(int tradeRandom, int tradeDetailNo,
			int exist, int saveSuccess) {
		List<Trade> listTrade = tradeService.queryByTradeNo(tradeRandom);
		if (listTrade.size() > 0) {
			Trade trade = listTrade.get(0);
			List<TradeDetail> list = tradeDetailService.queryByFkTradeId(trade
					.getTrace());
			for (int i = 0; i < list.size(); i++) {
				TradeDetail tradeDetail = list.get(i);
				if ((tradeDetailNo + "").equals(tradeDetail.getId())) {
					tradeDetail.setIs_upload(0);
					tradeDetailService.saveTradeDetail(tradeDetail);
				}
			}
		}
	}

}
