package com.eastcom.social.pos.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.dao.TradeFileDao;
import com.eastcom.social.pos.core.orm.dao.TradeFileDao.Properties;
import com.eastcom.social.pos.core.orm.entity.TradeFile;

public class TradeFileService {

	private static TradeFileService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private TradeFileDao tradeFileDao;

	private TradeFileService() {
	}

	public static TradeFileService getInstance(Context context) {
		if (instance == null) {
			instance = new TradeFileService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.tradeFileDao = instance.mDaoSession
					.getTradeFileDao();
		}
		return instance;
	}

	public TradeFile loadTradeFile(String id) {
		return tradeFileDao.load(id);
	}

	public List<TradeFile> loadAllTradeFile() {
		return tradeFileDao.loadAll();
	}

	public void deleteTradeFile(String id) {
		tradeFileDao.deleteByKey(id);
	}
	public void deleteAllTradeFile() {
		tradeFileDao.deleteAll();
	}


	public long saveTradeFile(TradeFile tradeFile) {
		return tradeFileDao.insertOrReplace(tradeFile);
	}

	public void saveTradeFiles(final List<TradeFile> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		tradeFileDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					TradeFile tradeFile = list.get(i);
					tradeFileDao.insertOrReplace(tradeFile);
				}
			}
		});

	}

	
	public List<TradeFile> queryByTradeStatus(int status) {
		List<TradeFile> list = new ArrayList<TradeFile>();
		list = tradeFileDao.queryBuilder().where(Properties.UploadStatus.eq(status))
				.list();
		return list;
	}
	
	/**
	 * 根据交易时间查询 
	 */
	public List<TradeFile> queryByTradeTime(Date date) {
		List<TradeFile> list = new ArrayList<TradeFile>();
		list = tradeFileDao.queryBuilder().where(Properties.TradeDate.eq(date))
				.list();
		return list;
	}

}
