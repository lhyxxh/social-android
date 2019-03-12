package com.eastcom.social.pos.core.service;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.eastcom.social.pos.core.orm.dao.BlackListDao;
import com.eastcom.social.pos.core.orm.dao.BlackListDao.Properties;
import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.entity.BlackList;

public class BlackListService {

	private static final String TAG = BlackListService.class.getSimpleName();
	private static BlackListService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private BlackListDao blackListDao;

	private BlackListService() {
	}

	public static BlackListService getInstance(Context context) {
		if (instance == null) {
			instance = new BlackListService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.blackListDao = instance.mDaoSession.getBlackListDao();
		}
		return instance;
	}

	public BlackList loadBlackList(String id) {
		return blackListDao.load(id);
	}

	/**
	 * 查询所有黑名单
	 * @return
	 */
	public List<BlackList> loadAllBlackList() {
		return blackListDao.loadAll();
	}


	public long saveBlackList(BlackList blackList) {
		return blackListDao.insertOrReplace(blackList);
	}

	public void saveBlackLists(final List<BlackList> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		blackListDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					BlackList blackList = list.get(i);
					blackListDao.insertOrReplace(blackList);
				}
			}
		});

	}

	public void deleteAllBlackList() {
		blackListDao.deleteAll();
	}

	public void deleteBlackList(String id) {
		blackListDao.deleteByKey(id);
		Log.i(TAG, "delete");
	}

	public void deleteBlackList(BlackList health) {
		blackListDao.delete(health);
	}
	/**
	 * 查询所有有效黑名单
	 * @return
	 */
	public List<BlackList> queryBlackList() {
		List<BlackList> list = blackListDao.queryBuilder()
				.where(Properties.Status.eq(1)).list();
		return list;
	}
	/**
	 * 查询所有无效黑名单
	 * @return
	 */
	public List<BlackList> queryWhiteList() {
		List<BlackList> list = blackListDao.queryBuilder()
				.where(Properties.Status.eq(0)).list();
		return list;
	}

}
