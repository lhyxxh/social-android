package com.eastcom.social.pos.core.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.dao.UnknowMedicineDao;
import com.eastcom.social.pos.core.orm.entity.UnknowMedicine;

public class UnknowMedicineService {

	private static UnknowMedicineService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private UnknowMedicineDao unknowMedicineDao;

	private UnknowMedicineService() {
	}

	public static UnknowMedicineService getInstance(Context context) {
		if (instance == null) {
			instance = new UnknowMedicineService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.unknowMedicineDao = instance.mDaoSession
					.getUnknowMedicineDao();
		}
		return instance;
	}

	public UnknowMedicine loadMedicine(String id) {
		return unknowMedicineDao.load(id);
	}

	public List<UnknowMedicine> loadAllMedicine() {
		return unknowMedicineDao.loadAll();
	}

	public void deleteMedicine(String id) {
		unknowMedicineDao.deleteByKey(id);
	}

	public long saveUnknowMedicine(UnknowMedicine medicine) {
		return unknowMedicineDao.insertOrReplace(medicine);
	}

	public void saveMedicineLists(final List<UnknowMedicine> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		unknowMedicineDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					UnknowMedicine unknowMedicine = list.get(i);
					unknowMedicineDao.insertOrReplace(unknowMedicine);
				}
			}
		});

	}

	public List<UnknowMedicine> queryUploadTrade(int limit, int offset) {
		List<UnknowMedicine> list = new ArrayList<UnknowMedicine>();
		list = unknowMedicineDao.queryBuilder().limit(limit).offset(offset)
				.list();
		return list;
	}

}
