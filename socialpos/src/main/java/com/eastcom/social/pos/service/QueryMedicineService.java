package com.eastcom.social.pos.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.eastcom.social.pos.core.orm.entity.Medicine;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;
import com.eastcom.social.pos.core.orm.entity.UnknowMedicine;
import com.eastcom.social.pos.core.service.MedicineService;
import com.eastcom.social.pos.core.service.TradeDetailService;
import com.eastcom.social.pos.core.service.UnknowMedicineService;
import com.eastcom.social.pos.core.socket.client.SoClient;
import com.eastcom.social.pos.core.socket.listener.ActivityCallBackListener;
import com.eastcom.social.pos.core.socket.message.SoMessage;
import com.eastcom.social.pos.core.socket.message.querymedicine.QueryMedicineMessage;
import com.eastcom.social.pos.core.socket.message.querymedicine.QueryMedicineRespMessage;
import com.eastcom.social.pos.listener.MySoClient;
import com.eastcom.social.pos.util.MyLog;

/**
 * 查询未知药品服务
 * 
 * @author eronc
 *
 */
public class QueryMedicineService extends Service {

	private MedicineService medicineService;
	private UnknowMedicineService unknowMedicineService;
	private TradeDetailService tradeDetailService;
	private List<UnknowMedicine> unknowMedicines = new ArrayList<UnknowMedicine>(); 
	private SoClient client;

	private int operator = 0;;
	private GetDataTask getDataTask;

	@Override
	public void onCreate() {
		super.onCreate();
		medicineService = MedicineService.getInstance(this);
		unknowMedicineService = UnknowMedicineService.getInstance(this);
		tradeDetailService = TradeDetailService.getInstance(this);
		readEid();
	}

	private void readEid() {
		client = MySoClient.newInstance().getClient();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
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

	public class GetDataTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				unknowMedicines = unknowMedicineService.loadAllMedicine();
				queryMedicine();
			} catch (Exception e) {
				getDataTask = null;
				MyLog.e("QueryMedicineService", operator + "" + e.getMessage());
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			getDataTask = null;
		}

	}

	/**
	 * 查询药品
	 */
	private void queryMedicine() {
		if (unknowMedicines.size() > 0) {
			String barCode = unknowMedicines.get(0).getBarCode();
			QueryMedicineMessage queryMedicineMessage = new QueryMedicineMessage(
					barCode);
			client.sendMessage(queryMedicineMessage,
					new ActivityCallBackListener() {

						@Override
						public void callBack(SoMessage message) {
							try {
								QueryMedicineRespMessage queryMedicineRespMessage = new QueryMedicineRespMessage(
										message);
								if (queryMedicineRespMessage.isExist()) {
									//保存药品
									Medicine medicine = new Medicine();
									medicine.setEntity(medicine,
											queryMedicineRespMessage);
									medicineService.saveMedicine(medicine);
									//更新已有交易明细
									updateMedicine(queryMedicineRespMessage
											.getBarCode(),
											queryMedicineRespMessage.getName());
								} 
								// 删除未知药品目录
								unknowMedicineService
								.deleteMedicine(queryMedicineRespMessage
										.getBarCode());
								unknowMedicines.remove(0);
								queryMedicine();
							} catch (Exception e) {
								MyLog.w("QueryMedicineService", "QueryMedicineRespMessage Exception:" 
										+ e.getMessage());
							}

						}

						@Override
						public void doTimeOut() {
							unknowMedicines.remove(0);
							queryMedicine();
						}
					}, 3);
		}else {
			stopSelf();
		}
	}

	/**
	 * 更新药品
	 */
	private void updateMedicine(String barCode, String product_name) {
		List<TradeDetail> preList = tradeDetailService.queryByBarCode(barCode);
		List<TradeDetail> list = new ArrayList<TradeDetail>();
		for (int i = 0; i < preList.size(); i++) {
			TradeDetail tradeDetail = preList.get(i);
			tradeDetail.setProduct_name(product_name);
			list.add(tradeDetail);
		}
		tradeDetailService.saveTradeDetailLists(list);
	}

}
