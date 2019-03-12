package com.eastcom.social.pos.core.service;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.dao.TempBlackListDao;
import com.eastcom.social.pos.core.orm.dao.TempBlackListDao.Properties;
import com.eastcom.social.pos.core.orm.entity.TempBlackList;
import com.eastcom.social.pos.core.orm.entity.Trade;

public class TempBlackListService {

	private static final String TAG = TempBlackListService.class.getSimpleName();
	private static TempBlackListService instance;
	private DaoSession mDaoSession;
	private TempBlackListDao tempBlackListDao;

	private TempBlackListService() {
	}

	public synchronized static TempBlackListService getInstance(Context context) {
		if (instance == null) {
			instance = new TempBlackListService();
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.tempBlackListDao = instance.mDaoSession.getTempBlackListDao();
		}
		return instance;
	}

	public TempBlackList loadTempBlackList(String id) {
		return tempBlackListDao.load(id);
	}

	public List<TempBlackList> loadAllTempBlackLists() {
		return tempBlackListDao.loadAll();
	}

	public void saveTrade(final TempBlackList tempBlackList) {
		mDaoSession.clear();
		if (tempBlackList == null) {
			return;
		}
		tempBlackListDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				tempBlackListDao.insertOrReplace(tempBlackList);
			}
		});
	}

	public void saveTradeLists(final List<TempBlackList> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		tempBlackListDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (TempBlackList trade : list) {
					try {
						tempBlackListDao.insertOrReplace(trade);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
			}
		});
	}

	public void deleteAllTempBlackList() {
		tempBlackListDao.deleteAll();
	}
	
	public void deleteByKey(String id) {
		tempBlackListDao.deleteByKey(id);
		Log.i(TAG, "delete");
	}

	/**
	 * 根据参考号模糊查询
	 * 
	 * @param ref_no
	 * @return
	 */
	public boolean queryByTime(String socialCardNo,Date date) {
		boolean result = true;
		TempBlackList tempBlackList = tempBlackListDao.queryBuilder()
				.where(Properties.SocialCardNo.eq(socialCardNo)).unique();
		if (tempBlackList.getTime().after(date)) {
			result = false;
		}else{
			result =true;
		}
			
		return result;
	}

}
