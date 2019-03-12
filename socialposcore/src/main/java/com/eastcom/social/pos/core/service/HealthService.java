package com.eastcom.social.pos.core.service;

import java.util.List;














import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.dao.HealthDao;
import com.eastcom.social.pos.core.orm.dao.HealthDao.Properties;
import com.eastcom.social.pos.core.orm.entity.Health;

import android.content.Context;
import android.util.Log;

public class HealthService {

	private static final String TAG = HealthService.class.getSimpleName();
	private static HealthService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private HealthDao healthDao;

	private HealthService() {
	}

	public static HealthService getInstance(Context context) {
		if (instance == null) {
			instance = new HealthService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.healthDao = instance.mDaoSession.getHealthDao();
		}
		return instance;
	}

	public Health loadHealth(String id) {
		return healthDao.load(id);
	}

	public List<Health> loadAllHealth() {
		return healthDao.loadAll();
	}

	public List<Health> queryHealth(String where, String... params) {
		return healthDao.queryRaw(where, params);
	}

	public long saveHealth(Health health) {
		return healthDao.insertOrReplace(health);
	}

	public void saveHealthLists(final List<Health> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		healthDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					Health health = list.get(i);
					healthDao.insertOrReplace(health);
				}
			}
		});

	}

	public void deleteAllHealth() {
		healthDao.deleteAll();
	}

	public void deleteHealth(String id) {
		healthDao.deleteByKey(id);
		Log.i(TAG, "delete");
	}

	public void deleteHealth(Health health) {
		healthDao.delete(health);
	}

	public List<Health> queryById(String id) {
		List<Health> list = healthDao.queryBuilder().where(Properties.Id.eq(id))
				.list();
		return list;
	}
	public List<Health> queryByName(String text) {
		List<Health> list = healthDao.queryBuilder().where(Properties.Name.eq(text))
				.list();
		return list;
	}
	public List<Health> queryByBarCode(String text) {
		List<Health> list = healthDao.queryBuilder().where(Properties.Bar_Code.eq(text))
				.list();
		return list;
	}
	public boolean isExist(String text) {
		boolean result = false;
		long count = healthDao.queryBuilder().where(Properties.Bar_Code.eq(text)).count();
		if (count > 0) {
			result = true;
		}
		return result;
	}

}
