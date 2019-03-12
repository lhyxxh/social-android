package com.eastcom.social.pos.core.service;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.dao.MedicineDao;
import com.eastcom.social.pos.core.orm.dao.MedicineDao.Properties;
import com.eastcom.social.pos.core.orm.entity.Medicine;

public class MedicineService {

	private static final String TAG = MedicineService.class.getSimpleName();
	private static MedicineService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private MedicineDao medicineDao;

	private MedicineService() {
	}

	public static MedicineService getInstance(Context context) {
		if (instance == null) {
			instance = new MedicineService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.medicineDao = instance.mDaoSession.getMedicineDao();
		}
		return instance;
	}

	public Medicine loadMedicine(String id) {
		return medicineDao.load(id);
	}

	public List<Medicine> loadAllMedicine() {
		return medicineDao.loadAll();
	}

	public List<Medicine> queryMedicine(String where, String... params) {
		return medicineDao.queryRaw(where, params);
	}

	public long saveMedicine(Medicine medicine) {
		return medicineDao.insertOrReplace(medicine);
	}

	public void saveMedicineLists(final List<Medicine> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		medicineDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					Medicine medicine = list.get(i);
					medicineDao.insertOrReplace(medicine);
				}
			}
		});

	}

	public void deleteAllMedicine() {
		medicineDao.deleteAll();
	}

	public void deleteMedicine(String id) {
		medicineDao.deleteByKey(id);
		Log.i(TAG, "delete");
	}

	public void deleteMedicine(Medicine medicine) {
		medicineDao.delete(medicine);
	}

	public List<Medicine> queryById(String id) {
		List<Medicine> list = medicineDao.queryBuilder().where(Properties.Id.eq(id))
				.list();
		return list;
	}
	public List<Medicine> queryByName(String text) {
		List<Medicine> list = medicineDao.queryBuilder().where(Properties.Name.eq(text))
				.list();
		return list;
	}
	public List<Medicine> queryByBarCode(String text) {
		List<Medicine> list = medicineDao.queryBuilder().where(Properties.Bar_Code.eq(text))
				.list();
		return list;
	}
	public List<Medicine> queryLikeBarCode(String text){
		List<Medicine> list = medicineDao.queryBuilder().where(Properties.Bar_Code.like("%"+text+"%"))
				.list();
		return list;
	}
	public boolean isExist(String text) {
		boolean result = false;
		long count = medicineDao.queryBuilder().where(Properties.Bar_Code.eq(text)).count();
		if (count > 0) {
			result = true;
		}
		return result;
	}

}
