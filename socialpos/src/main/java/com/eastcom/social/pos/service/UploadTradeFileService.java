package com.eastcom.social.pos.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;

import com.eastcom.social.pos.application.MyApplicationLike;
import com.eastcom.social.pos.core.orm.entity.Trade;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.core.orm.entity.TradeFile;
import com.eastcom.social.pos.core.service.TradeDetailService;
import com.eastcom.social.pos.core.service.TradeFileService;
import com.eastcom.social.pos.core.service.TradeService;
import com.eastcom.social.pos.core.utils.ByteUtils;
import com.eastcom.social.pos.entity.UploadTradeDetailEntity;
import com.eastcom.social.pos.entity.UploadTradeEntity;
import com.eastcom.social.pos.util.FileUtils;
import com.eastcom.social.pos.util.MD5;
import com.eastcom.social.pos.util.MyLog;
import com.eastcom.social.pos.util.TimeUtil;
import com.google.gson.Gson;

/**
 * 上传交易文件服务
 * 
 * @author eronc
 *
 */
@SuppressLint("SimpleDateFormat")
public class UploadTradeFileService extends Service {

	private TradeService tradeService;
	private TradeDetailService tradeDetailService;
	private GetDataTask getDataTask;
	private String appid;

	@Override
	public void onCreate() {
		super.onCreate();
		tradeService = TradeService.getInstance(this);
		tradeDetailService = TradeDetailService.getInstance(this);
		appid = LocalDataFactory.newInstance(MyApplicationLike.getContext())
				.getString(LocalDataFactory.EID, "");

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// appid = intent.getStringExtra("appid");
		startGetDataTask();
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

	private int operateType = 0;

	public class GetDataTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				if (operateType == 0) {
					zipFile();
				} else if (operateType == 1) {
					upload();
				}
				return true;
			} catch (Exception e) {
				MyLog.e("UploadTradeFileService",
						operateType + ":" + e.getMessage());
				return false;

			}

		}

		@Override
		protected void onPostExecute(Boolean result) {
			getDataTask = null;
			if (operateType == 0) {
				operateType = 1;
				startGetDataTask();
			}
		}

	}

	private boolean upload() {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String secret = "pdsm12K0prj7SpoRfGy32DswwX098aXa";
			String timestamp = format.format(new Date());
			String sign = appid + secret + timestamp;

			pathFileExist();

			List<TradeFile> list = TradeFileService.getInstance(
					MyApplicationLike.getContext()).queryByTradeStatus(0);
			for (int i = 0; i < list.size(); i++) {
				TradeFile tradeFile = list.get(i);
				String desFilePath = tradeFile.getTradeFileName();
				File desFile = new File(desFilePath);
				if (!desFile.exists()) {
					return false;
				}

				Date tradeDate = tradeFile.getTradeDate();
				String tradeDateString = TimeUtil.getDateString(tradeDate);
				int tradeType = tradeFile.getTradeType();
				String jsonString = DataFactory.uploadTradeFile(desFile, appid,
						timestamp, MD5.getMD5Code(sign).toUpperCase(),
						tradeDateString, tradeType);
				JSONObject jsonObject = new JSONObject(jsonString);
				String code = jsonObject.getString("code");
				MyLog.i("UploadTradeFileService", "ok:" + code);
				if (code.equals("0")) {
					TradeFile tradeFile1 = TradeFileService.getInstance(
							MyApplicationLike.getContext()).loadTradeFile(
							desFilePath);
					tradeFile1.setUploadStatus(1);
					TradeFileService.getInstance(MyApplicationLike.getContext())
							.saveTradeFile(tradeFile1);
				}

			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void zipFile() {
		// 每个文件包含的交易数量
		List<Trade> list = TradeService.getInstance(MyApplicationLike.getContext())
				.loadAllTradeOrderByTradeTime();
		if (list.size() > 0) {
			// 查询最后一笔交易时间
			Date lastDate = list.get(0).getTrade_time();
			// 最后一天是否已经压缩
			List<TradeFile> tradeFiles = TradeFileService.getInstance(
					MyApplicationLike.getContext()).queryByTradeTime(lastDate);
			if (tradeFiles.size() == 0) {

				int unit = 10;
				Date dateStart = TimeUtil.getDate(TimeUtil
						.getDateStringBeforeDay(lastDate, 0));
				Date dateEnd = TimeUtil.getDate(TimeUtil
						.getDateStringBeforeDay(lastDate, 1));
				List<Trade> tradesY = tradeService.queryByDuringTime(dateStart,
						dateEnd);
				int count = tradesY.size() % unit == 0 ? tradesY.size() / unit
						: tradesY.size() / unit + 1;
				for (int j = 0; j < count; j++) {

					String no2 = ByteUtils.addZeroStr(String.valueOf(j + 1), 2,
							true);
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					// 加减天
					String todayString = format.format(cal.getTime());

					String filePath = pathFileExist();
					String desFilePath = filePath + todayString + "_" + no2
							+ ".zip";
					File desFile = new File(desFilePath);
					if (!desFile.exists()) {
						try {
							desFile.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
						// 开始一次压缩
						List<UploadTradeDetailEntity> uploadTradeDetailEntityList = new ArrayList<UploadTradeDetailEntity>();
						UploadTradeEntity uploadTradeEntity = new UploadTradeEntity();

						List<Trade> trades = tradeService.queryUploadTrade(
								dateStart, dateEnd, unit, j * unit);
						List<UploadTradeEntity> uploadTradeEntityList = new ArrayList<UploadTradeEntity>();
						for (int i = 0; i < trades.size(); i++) {
							// 一条交易数据
							Trade trade = trades.get(i);
							List<TradeDetail> tradeDetails = tradeDetailService
									.queryByFkTradeId(trade.getId());
							for (int k = 0; k < tradeDetails.size(); k++) {
								// 交易明细数据
								TradeDetail tradeDetail = tradeDetails.get(k);
								UploadTradeDetailEntity uploadTradeDetailEntity = new UploadTradeDetailEntity();
								uploadTradeDetailEntity
										.setActualPrice(tradeDetail
												.getActual_price());
								uploadTradeDetailEntity.setAmount(tradeDetail
										.getAmount());
								uploadTradeDetailEntity.setBarCode(tradeDetail
										.getBar_code());
								uploadTradeDetailEntity
										.setSocialCategory(tradeDetail
												.getSocial_category());
								uploadTradeDetailEntity
										.setSuperVisionCode(tradeDetail
												.getSuper_vision_code());
								uploadTradeDetailEntity.setTradeDetailNo(k);

								uploadTradeDetailEntityList
										.add(uploadTradeDetailEntity);
							}
							// 交易汇总
							uploadTradeEntity.setBankCardNo(trade
									.getBank_card_no());
							uploadTradeEntity
									.setIdCardNo(trade.getId_card_no());
							uploadTradeEntity.setMac("1111");
							uploadTradeEntity
									.setPreTradeNo(trade.getTrade_no() - 1);
							uploadTradeEntity.setPsamNo(trade.getPsam_no());
							uploadTradeEntity.setRandom(1);
							uploadTradeEntity.setRefNo(trade.getRef_no());
							uploadTradeEntity.setRfsamNo(trade.getRfsam_no());
							uploadTradeEntity.setSbCardNo(trade
									.getSocial_card_no());
							uploadTradeEntity.setTradeMoney(trade
									.getTrade_money());
							uploadTradeEntity.setTradeSn("00000");
							uploadTradeEntity.setTradeState(70);
							uploadTradeEntity.setTradeTime(trade
									.getTrade_time());
							uploadTradeEntity.setTradeType(14);
							uploadTradeEntity
									.setDetail(uploadTradeDetailEntityList);
							uploadTradeEntityList.add(uploadTradeEntity);
						}
						Gson gson = new Gson();
						String json = gson.toJson(uploadTradeEntityList);

						String no1 = ByteUtils.addZeroStr(
								String.valueOf(j + 1), 4, true);
						String srcFilePath = filePath + todayString + "_" + no1
								+ ".json";
						File srcFile = new File(srcFilePath);
						if (!srcFile.exists()) {
							try {
								srcFile.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						try {
							FileUtils.writeToFile(json, srcFile);
						} catch (Exception e) {
							e.printStackTrace();
						}
						FileUtils.zipFile(desFile, srcFile);

						TradeFile tradeFile = new TradeFile(desFilePath, 1,
								dateStart, 0);
						TradeFileService
								.getInstance(MyApplicationLike.getContext())
								.saveTradeFile(tradeFile);
					}
				}
			}
		}
	}

	private String pathFileExist() {
		String filePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/social-pos/";
		File pathFile = new File(filePath);
		if (!pathFile.exists()) {
			pathFile.mkdir();
		}
		return filePath;
	}

}
