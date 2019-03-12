package com.eastcom.social.pos.core.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.dao.PolicyDocumentDao;
import com.eastcom.social.pos.core.orm.dao.PolicyDocumentDao.Properties;
import com.eastcom.social.pos.core.orm.entity.PolicyDocument;

public class PolicyDocumentService {

	private static final String TAG = PolicyDocumentService.class.getSimpleName();
	private static PolicyDocumentService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private PolicyDocumentDao policyDocumentDao;

	private PolicyDocumentService() {
	}

	public static PolicyDocumentService getInstance(Context context) {
		if (instance == null) {
			instance = new PolicyDocumentService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.policyDocumentDao = instance.mDaoSession.getPolicyDocumentDao();
		}
		return instance;
	}

	public PolicyDocument loadPolicyDocument(String id) {
		return policyDocumentDao.load(id);
	}

	public List<PolicyDocument> loadAllPolicyDocument() {
		return policyDocumentDao.loadAll();
	}

	public List<PolicyDocument> queryPolicyDocument(String where, String... params) {
		return policyDocumentDao.queryRaw(where, params);
	}

	public long savPolicyDocument(PolicyDocument PolicyDocument) {
		return policyDocumentDao.insertOrReplace(PolicyDocument);
	}

	public void savePolicyDocumentLists(final ArrayList<PolicyDocument> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		policyDocumentDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					PolicyDocument PolicyDocument = list.get(i);
					policyDocumentDao.insertOrReplace(PolicyDocument);
				}
			}
		});

	}

	public void deleteAllPolicyDocument() {
		policyDocumentDao.deleteAll();
	}

	public void deletePolicyDocument(String id) {
		policyDocumentDao.deleteByKey(id);
		Log.i(TAG, "delete");
	}

	public void delePolicyDocument(PolicyDocument health) {
		policyDocumentDao.delete(health);
	}

	public List<PolicyDocument> loadPolicyDocumentByVersion(String version) {
		List<PolicyDocument> list = policyDocumentDao.queryBuilder()
				.where(Properties.VersionCode.eq(version)).list();
		return list;
	}
}
