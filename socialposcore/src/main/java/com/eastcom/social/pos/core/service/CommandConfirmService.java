package com.eastcom.social.pos.core.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.eastcom.social.pos.core.orm.dao.CommandConfirmDao;
import com.eastcom.social.pos.core.orm.dao.Dao;
import com.eastcom.social.pos.core.orm.dao.DaoSession;
import com.eastcom.social.pos.core.orm.entity.CommandConfirm;

public class CommandConfirmService {

	private static final String TAG = CommandConfirmService.class.getSimpleName();
	private static CommandConfirmService instance;
	private static Context appContext;
	private DaoSession mDaoSession;
	private CommandConfirmDao commandConfirmDao;

	private CommandConfirmService() {
	}

	public static CommandConfirmService getInstance(Context context) {
		if (instance == null) {
			instance = new CommandConfirmService();
			if (appContext == null) {
				appContext = context.getApplicationContext();
			}
			instance.mDaoSession = Dao.getDaoSession(context);
			instance.commandConfirmDao = instance.mDaoSession.getCommandConfirmDao();
		}
		return instance;
	}

	public CommandConfirm loadCommandConfirm(String id) {
		return commandConfirmDao.load(id);
	}

	public List<CommandConfirm> loadAllCommandConfirm() {
		return commandConfirmDao.loadAll();
	}

	public List<CommandConfirm> queryCommandConfirm(String where, String... params) {
		return commandConfirmDao.queryRaw(where, params);
	}

	public long saveCommandConfirm(CommandConfirm commandConfirm) {
		return commandConfirmDao.insertOrReplace(commandConfirm);
	}

	public void saveCommandConfirmLists(final ArrayList<CommandConfirm> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		commandConfirmDao.getSession().runInTx(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < list.size(); i++) {
					CommandConfirm commandConfirm = list.get(i);
					commandConfirmDao.insertOrReplace(commandConfirm);
				}
			}
		});

	}

	public void deleteAllCommandConfirm() {
		commandConfirmDao.deleteAll();
	}

	public void deleteCommandConfirm(String id) {
		commandConfirmDao.deleteByKey(id);
		Log.i(TAG, "delete");
	}

	public void deleteCommandConfirm(CommandConfirm commandConfirm) {
		commandConfirmDao.delete(commandConfirm);
	}

}
