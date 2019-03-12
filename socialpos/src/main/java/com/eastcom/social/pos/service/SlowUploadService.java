package com.eastcom.social.pos.service;

import java.util.Date;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;

import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.core.service.TradeDetailService;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.message.SoTradeType;
import com.eastcom.social.pos.core.socket.message.trade.FinanceTradeMessage;
import com.eastcom.social.pos.core.socket.message.trade.SocialTradeMessage;
import com.eastcom.social.pos.core.socket.message.tradedetail.TradeDetailMessage;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.receiver.AlarmReceiver;
import com.eastcom.social.pos.util.MyLog;

/**
 * 定时上传未上传成功服务
 * 
 * @author eronc
 *
 */
public class SlowUploadService extends Service {

	private TradeService tradeService;
	private TradeDetailService tradeDetailService;
	private GetDataTask getDataTask;

	private SoClient client;

	@Override
	public void onCreate() {
		super.onCreate();
		tradeService = TradeService.getInstance(this);
		tradeDetailService = TradeDetailService.getInstance(this);
		readEid();
	}

	private void readEid() {
		client = MySoClient.newInstance().getClient();
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startGetDataTask();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);


		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void startGetDataTask() {
		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute((Void) null);
		}
	}

	public class GetDataTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				upload();
			} catch (Exception e) {
			}
			
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			getDataTask = null;
		}

	}

	private void upload() {
		uploadTradeService();
		uploadTradeDetailService();
	}

	private void uploadTradeService() {
		List<Trade> list = tradeService.queryByIsUpload(true);
		MyLog.i("slow upload", "size" + list.size());
		for (int i = 0; i < list.size(); i++) {
			Trade trade = list.get(i);
			sendTradeMessage(trade);
			int send_count = trade.getSend_count();
			trade.setSend_count(send_count + 1);
			tradeService.saveTrade(trade);
		}
	}

	private void uploadTradeDetailService() {
		List<TradeDetail> list = tradeDetailService.queryByIsUpload();
		for (int i = 0; i < list.size(); i++) {
			List<Trade> list2 = tradeService.queryById(list.get(i)
					.getFk_trade_id());
			if (list2.size() > 0) {
				Trade trade = tradeService.queryById(
						list.get(i).getFk_trade_id()).get(0);
				sendTradeDetailMessage(trade, list.get(i));
			}

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
			MyLog.e("UploadService TradeDetailMessage", "" + e.getMessage());
		}

	}

	/**
	 * 明文上传交易
	 * 
	 * @param mTrade
	 */
	private void sendTradeMessage(Trade mTrade) {
		
		if (mTrade.getEncrypt() == null) {//只有社保卡消费需要验证是否加密上传
			String tradeState = mTrade.getTrade_state() == 71 ? "0071" : "0070";
			String isEncrypt = "00";
			int preNo = mTrade.getPre_trade_no();
			
			String bankCard = mTrade.getBank_card_no();
			if (bankCard == null || "".equals(bankCard)) {
				bankCard = "00000000000000000000";
			}
			String socialCardNo = mTrade.getSocial_card_no();
			if (socialCardNo.length() != 9) {
				socialCardNo = "M00000000";
			}
			String refNo = mTrade.getRef_no();
			Date tradeTime = mTrade.getTrade_time();
			String tradeSn = "000000";
			int tradeMoney = mTrade.getTrade_money();
			String psamNo = mTrade.getPsam_no();
			String terminalCode = mTrade.getTerminal_code();
			String rfsamNo = mTrade.getRfsam_no();
			int tradeRandom = mTrade.getTrade_no();
			int mac = 10;
			int sendCount = mTrade.getSend_count();
			int tradeNo = mTrade.getTrade_no();
			MyLog.i("slow socialCardNo",
					socialCardNo + "----" + bankCard + mTrade.getTrade_type());
			if (mTrade.getTrade_type() == Integer.valueOf(SoTradeType.金融交易_82字节)) {
				String tradeType = SoTradeType.金融交易_82字节;
				FinanceTradeMessage financeTradeMessage = new FinanceTradeMessage(
						tradeType, tradeState, isEncrypt, preNo, bankCard,
						socialCardNo, refNo, tradeTime, tradeSn, tradeMoney,
						psamNo, terminalCode, rfsamNo, tradeRandom, mac, sendCount,
						tradeNo);

				client.sendMessage(financeTradeMessage);
			} else if (mTrade.getTrade_type() == Integer
					.valueOf(SoTradeType.医保交易_82字节)) {
				String tradeType = SoTradeType.医保交易_82字节;
				SocialTradeMessage socialTradeRespMessage = new SocialTradeMessage(
						tradeType, tradeState, isEncrypt, preNo, bankCard,
						socialCardNo, refNo, tradeTime, tradeSn, tradeMoney,
						psamNo, terminalCode, rfsamNo, tradeRandom, mac, sendCount,
						tradeNo);

				client.sendMessage(socialTradeRespMessage);
			} else if (mTrade.getTrade_type() == Integer
					.valueOf(SoTradeType.医保交易撤单_82字节)) {
				String tradeType = SoTradeType.医保交易撤单_82字节;
				SocialTradeMessage socialTradeRespMessage = new SocialTradeMessage(
						tradeType, tradeState, isEncrypt, preNo, bankCard,
						socialCardNo, refNo, tradeTime, tradeSn, tradeMoney,
						psamNo, terminalCode, rfsamNo, tradeRandom, mac, sendCount,
						tradeNo);

				client.sendMessage(socialTradeRespMessage);
			} else if (mTrade.getTrade_type() == 11) {
				String tradeType = SoTradeType.金融交易撤单_82字节;
				FinanceTradeMessage financeTradeMessage = new FinanceTradeMessage(
						tradeType, tradeState, isEncrypt, preNo, bankCard,
						socialCardNo, refNo, tradeTime, tradeSn, tradeMoney,
						psamNo, terminalCode, rfsamNo, tradeRandom, mac, sendCount,
						tradeNo);
				client.sendMessage(financeTradeMessage);
			}	
		}else {
			String tradeState = mTrade.getTrade_state() == 71 ? "0071" : "0070";
			String isEncrypt = "01";
			int preNo = mTrade.getPre_trade_no();
			String rfsamNo = mTrade.getRfsam_no();
			int tradeRandom = mTrade.getTrade_no();
			int mac = 10;
			int sendCount = 1;
			int tradeNo = mTrade.getTrade_no();
			String tradeType;
			if (mTrade.getTrade_type() == Integer.valueOf(SoTradeType.医保交易_82字节)) {
				tradeType = SoTradeType.医保交易_82字节;
			} else {
				tradeType = SoTradeType.医保交易撤单_82字节;
			}
			String cipherText = mTrade.getEncrypt();
			SocialTradeMessage socialTradeRespMessage = new SocialTradeMessage(
					tradeType, tradeState, isEncrypt, preNo, rfsamNo, tradeRandom,
					mac, sendCount, tradeNo, cipherText);
			client.sendMessage(socialTradeRespMessage);
		}
	}

	/**
	 * 密文上传交易
	 * 
	 * @param mTrade
	 * @param cipherText
	 */
	private void sendTradeMessageCipher(Trade mTrade, String cipherText) {
		String tradeState = mTrade.getTrade_state() == 71 ? "0071" : "0070";
		String isEncrypt = "01";
		int preNo = mTrade.getPre_trade_no();
		String rfsamNo = mTrade.getRfsam_no();
		int tradeRandom = mTrade.getTrade_no();
		int mac = 10;
		int sendCount = 1;
		int tradeNo = mTrade.getTrade_no();
		String tradeType;

		if (mTrade.getTrade_type() == Integer.valueOf(SoTradeType.医保交易_82字节)) {
			tradeType = SoTradeType.医保交易_82字节;
			SocialTradeMessage socialTradeRespMessage = new SocialTradeMessage(
					tradeType, tradeState, isEncrypt, preNo, rfsamNo,
					tradeRandom, mac, sendCount, tradeNo, cipherText);

			client.sendMessage(socialTradeRespMessage);
		} else if (mTrade.getTrade_type() == Integer
				.valueOf(SoTradeType.医保交易撤单_82字节)) {
			tradeType = SoTradeType.医保交易撤单_82字节;
			SocialTradeMessage socialTradeRespMessage = new SocialTradeMessage(
					tradeType, tradeState, isEncrypt, preNo, rfsamNo,
					tradeRandom, mac, sendCount, tradeNo, cipherText);

			client.sendMessage(socialTradeRespMessage);
		}

	}
}
