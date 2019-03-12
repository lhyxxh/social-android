package com.eastcom.social.pos.core.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.dao.DocumentDao;
import com.eastcom.social.pos.core.orm.dao.DocumentDao.Properties;
import com.eastcom.social.pos.core.orm.entity.Document;

public class DocumentService {

	private static final String TAG = DocumentService.class.getSimpleName();
	private static DocumentService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private DocumentDao documentDao;

	private DocumentService() {
	}

	public static DocumentService getInstance(Context context) {
		if (instance == null) {
			instance = new DocumentService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.documentDao = instance.mDaoSession.getDocumentDao();
		}
		return instance;
	}

	public Document loadDocument(String id) {
		return documentDao.load(id);
	}

	public List<Document> loadAllDocument() {
		return documentDao.loadAll();
	}

	public List<Document> queryDocument(String where, String... params) {
		return documentDao.queryRaw(where, params);
	}

	public long savDocument(Document Document) {
		return documentDao.insertOrReplace(Document);
	}

	public void saveDocumentLists(final ArrayList<Document> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		documentDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					Document Document = list.get(i);
					documentDao.insertOrReplace(Document);
				}
			}
		});

	}

	public void deleteAllDocument() {
		documentDao.deleteAll();
	}

	public void deleteDocument(String id) {
		documentDao.deleteByKey(id);
		Log.i(TAG, "delete");
	}

	public void deleDocument(Document health) {
		documentDao.delete(health);
	}

	public List<Document> loadDocumentByFkFileId(String fkFileId) {
		List<Document> list = documentDao.queryBuilder()
				.where(Properties.FkFileId.eq(fkFileId)).list();
		return list;
	}
	public List<Document> loadDocumentNotRead() {
		List<Document> list = documentDao.queryBuilder()
				.where(Properties.IdRead.eq(0)).list();
		return list;
	}
	public void saveReadDocument(ArrayList<String> fkFileIds) {
		for (int i = 0; i < fkFileIds.size(); i++) {
			String fkFileId = fkFileIds.get(i);
			List<Document> list = documentDao.queryBuilder()
					.where(Properties.FileName.eq(fkFileId)).list();
			for (int j = 0; j < list.size(); j++) {
				Document document = list.get(j);
				document.setIdRead(1);
				savDocument(document);
			}
		}
	}
}
