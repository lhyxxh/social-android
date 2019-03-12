package com.eastcom.social.pos.core.service;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.eastcom.social.pos.core.orm.dao.CardInfoDao;
import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.entity.CardInfo;

public class CardInfoService {

	private static final String TAG = CardInfoService.class.getSimpleName();
	private static CardInfoService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private CardInfoDao cardInfoDao;

	private CardInfoService() {
	}

	public static CardInfoService getInstance(Context context) {
		if (instance == null) {
			instance = new CardInfoService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.cardInfoDao = instance.mDaoSession.getCardInfoDao();
		}
		return instance;
	}

	public CardInfo loadCardInfo(String id) {
		return cardInfoDao.load(id);
	}

	public List<CardInfo> loadAllCardInfo() {
		return cardInfoDao.loadAll();
	}

	public List<CardInfo> queryCardInfo(String where, String... params) {
		return cardInfoDao.queryRaw(where, params);
	}

	public long savCardInfo(CardInfo cardInfo) {
		return cardInfoDao.insertOrReplace(cardInfo);
	}

	public void saveCardInfoLists(final List<CardInfo> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		cardInfoDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					CardInfo cardInfo = list.get(i);
					cardInfoDao.insertOrReplace(cardInfo);
				}
			}
		});

	}

	public void deleteAllCardInfo() {
		cardInfoDao.deleteAll();
	}

	public void deleteCardInfo(String id) {
		cardInfoDao.deleteByKey(id);
		Log.i(TAG, "delete");
	}

	public void deleCardInfo(CardInfo health) {
		cardInfoDao.delete(health);
	}


}
