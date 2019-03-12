package com.eastcom.social.pos.core.service;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.dao.TradeDetailDao;
import com.eastcom.social.pos.core.orm.dao.TradeDetailDao.Properties;
import com.eastcom.social.pos.core.orm.entity.TradeDetail;

public class TradeDetailService {

	private static final String TAG = TradeDetailService.class.getSimpleName();
	private static TradeDetailService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private TradeDetailDao tradeDetailDao;

	private TradeDetailService() {
	}

	public static TradeDetailService getInstance(Context context) {
		if (instance == null) {
			instance = new TradeDetailService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.tradeDetailDao = instance.mDaoSession.getTradeDetailDao();
		}
		return instance;
	}

	public TradeDetail loadTradeDetail(String id) {
		return tradeDetailDao.load(id);
	}

	public List<TradeDetail> loadAllTradeDetail() {
		return tradeDetailDao.loadAll();
	}


	public List<TradeDetail> queryTradeDetail(String where, String... params) {
		return tradeDetailDao.queryRaw(where, params);
	}

	public long saveTradeDetail(TradeDetail tradeDetail) {
		return tradeDetailDao.insertOrReplace(tradeDetail);
	}

	public void saveTradeDetailLists(final List<TradeDetail> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		tradeDetailDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					mDaoSession.clear();
					TradeDetail tradeDetail = list.get(i);
					tradeDetailDao.insertOrReplace(tradeDetail);
				}
			}
		});

	}

	public void deleteAllTradeDetail() {
		mDaoSession.clear();
		tradeDetailDao.deleteAll();
	}

	public void deleteTradeDetail(String id) {
		mDaoSession.clear();
		tradeDetailDao.deleteByKey(id);
		Log.i(TAG, "delete");
	}

	public void deleteTradeDetail(TradeDetail tradeDetail) {
		mDaoSession.clear();
		tradeDetailDao.delete(tradeDetail);
	}

	public TradeDetail queryById(String id) {
		mDaoSession.clear();
		TradeDetail list = tradeDetailDao.queryBuilder().where(Properties.Id.eq(id))
				.unique();
		return list;
	}
	/**
	 * 通过所属交易汇总信息查询对应交易详情
	 * @param fk_trade_id
	 * @return
	 */
	public List<TradeDetail> queryByFkTradeId(String fk_trade_id) {
		mDaoSession.clear();
		List<TradeDetail> list = tradeDetailDao.queryBuilder().where(Properties.Fk_Trade_Id.eq(fk_trade_id))
				.list();
		return list;
	}
	
	/**
	 * 通过所属交易汇总信息查询对应交易详情
	 * @param fk_trade_id
	 * @return
	 */
	public List<TradeDetail> queryByBarCode(String barCode) {
		mDaoSession.clear();
		List<TradeDetail> list = tradeDetailDao.queryBuilder().where(Properties.Bar_Code.eq(barCode))
				.list();
		return list;
	}
	/**
	 * 通过所属交易汇总信息查询对应交易详情
	 * @param fk_trade_id
	 * @return
	 */
	public List<TradeDetail> queryByFkTradeIdAndDetailNo(String fk_trade_id,String tradeDetailNo) {
		mDaoSession.clear();
		List<TradeDetail> list = tradeDetailDao.queryBuilder().where(Properties.Fk_Trade_Id.eq(fk_trade_id))
				.list();
		return list;
	}
	
	/**
	 * 获取没有上传成功的交易明细列表
	 * @return
	 */
	public List<TradeDetail> queryByIsUpload() {
		mDaoSession.clear();
		List<TradeDetail> list = tradeDetailDao
				.queryBuilder()
				.where(Properties.Is_Upload.eq(1)).list();
		
		return list;
	}
	
	

}
