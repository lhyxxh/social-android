package com.eastcom.social.pos.core.service;

import java.util.List;

import android.content.Context;

import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.dao.OmniTradeDao;
import com.eastcom.social.pos.core.orm.dao.OmniTradeDao.Properties;
import com.eastcom.social.pos.core.orm.entity.OmniTrade;

public class OmniTradeService {

	private static OmniTradeService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private OmniTradeDao omniTradeDao;

	private OmniTradeService() {
	}

	public static OmniTradeService getInstance(Context context) {
		if (instance == null) {
			instance = new OmniTradeService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.omniTradeDao = instance.mDaoSession.getOmniTradeDao();
		}
		return instance;
	}

	public OmniTrade loadOmniTrade(String id) {
		return omniTradeDao.load(id);
	}

	public List<OmniTrade> loadAllOmniTrade() {
		return omniTradeDao.loadAll();
	}

	public long saveOmniTrade(OmniTrade omniTrade) {
		return omniTradeDao.insertOrReplace(omniTrade);
	}

	public void saveOmniTradeLists(final List<OmniTrade> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		omniTradeDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					OmniTrade omniTrade = list.get(i);
					omniTradeDao.insertOrReplace(omniTrade);
				}
			}
		});

	}

	public void deleteAllOmniTrade() {
		omniTradeDao.deleteAll();
	}

	public OmniTrade queryLastTrade() {
		List<OmniTrade> list = omniTradeDao.queryBuilder()
				.orderDesc(Properties.PayTime).list();
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}

	}

}
