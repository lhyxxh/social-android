package com.eastcom.social.pos.core.orm.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

public class DaoMaster extends AbstractDaoMaster {
	public static final int SCHEMA_VERSION = 16;// 全渠道支付

	/** Creates underlying database table using DAOs. */
	public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
		TradeDao.createTable(db, ifNotExists);
		TradeDetailDao.createTable(db, ifNotExists);
		MedicineDao.createTable(db, ifNotExists);
		HealthDao.createTable(db, ifNotExists);
		CardInfoDao.createTable(db, ifNotExists);
		BlackListDao.createTable(db, ifNotExists);
		TempBlackListDao.createTable(db, ifNotExists);
		PolicyDocumentDao.createTable(db, ifNotExists);
		CommandConfirmDao.createTable(db, ifNotExists);
		DocumentDao.createTable(db, ifNotExists);
		UnknowMedicineDao.createTable(db, ifNotExists);
		TradeFileDao.createTable(db, ifNotExists);
		OmniTradeDao.createTable(db, ifNotExists);
	}

	/** Drops underlying database table using DAOs. */
	public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
		MedicineDao.dropTable(db, ifExists);
		HealthDao.dropTable(db, ifExists);
		TradeDao.dropTable(db, ifExists);
		TradeDetailDao.dropTable(db, ifExists);
		CardInfoDao.dropTable(db, ifExists);
		BlackListDao.dropTable(db, ifExists);
		TempBlackListDao.dropTable(db, ifExists);
		PolicyDocumentDao.dropTable(db, ifExists);
		CommandConfirmDao.dropTable(db, ifExists);
		DocumentDao.dropTable(db, ifExists);
		UnknowMedicineDao.dropTable(db, ifExists);
		TradeFileDao.dropTable(db, ifExists);
		OmniTradeDao.dropTable(db, ifExists);
	}

	public static abstract class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory, SCHEMA_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i("greenDAO", "Creating tables for schema version "
					+ SCHEMA_VERSION);
			createAllTables(db, false);
		}
	}

	/** WARNING: Drops all table on Upgrade! Use only during development. */
	public static class DevOpenHelper extends OpenHelper {
		public DevOpenHelper(Context context, String name, CursorFactory factory) {
			super(context, name, factory);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// trade表新增 encrypt 字段,加密数据
			boolean result1 = false;
			Cursor cursor1 = null;

			try {
				cursor1 = db
						.rawQuery(
								"select * from sqlite_master where name = ? and sql like ?",
								new String[] { "TRADE", "%" + "ENCRYPT" + "%" });
				result1 = null != cursor1 && cursor1.moveToFirst();
			} catch (Exception e) {
				Log.e("daoMaster", "checkColumnExists2...");
			} finally {
				if (null != cursor1 && !cursor1.isClosed()) {
					cursor1.close();
				}
			}
			if (!result1) {
				db.execSQL("ALTER TABLE 'TRADE' ADD 'ENCRYPT' INTEGER ");
			}

			// 新增后续指令表
			db.execSQL("CREATE TABLE  IF NOT EXISTS 'COMMAND_CONFIRM' (" + //
					"'ID' TEXT PRIMARY KEY NOT NULL ," + // 0: ID
					"'TYPE' INTEGER);"); // 1: type
			// 新增政策文件表
			db.execSQL("CREATE TABLE IF NOT EXISTS 'DOCUMENT' (" + //
					"'ID' TEXT PRIMARY KEY NOT NULL ," + // 0: id
					"'FILE_NAME' TEXT," + // 1: fileName
					"'FK_FILE_ID' TEXT," + // 2: fkFileId
					"'FILE_NO' INTEGER," + // 3: fileNo
					"'FK_SIGNBOARD_ID' TEXT," + // 4: fkSignboardId
					"'IS_DOWNLOAD' INTEGER," + // 5: isDownload
					"'ID_READ' INTEGER," + // 6: idRead
					"'CREATE_DATE' TEXT," + // 7: createDate
					"'DOWNLOAD' TEXT," + // 8: download
					"'READ_DATE' TEXT);"); // 9: readDate

			boolean result2 = false;
			Cursor cursor2 = null;
			// trade表新增 SEND_COUNT 字段
			try {
				cursor2 = db
						.rawQuery(
								"select * from sqlite_master where name = ? and sql like ?",
								new String[] { "TRADE",
										"%" + "SEND_COUNT" + "%" });
				result2 = null != cursor2 && cursor2.moveToFirst();
			} catch (Exception e) {
				Log.e("daoMaster", "checkColumnExists2..." + e.getMessage());
			} finally {
				if (null != cursor2 && !cursor2.isClosed()) {
					cursor2.close();
				}
			}
			if (!result2) {
				db.execSQL("ALTER TABLE 'TRADE' ADD 'SEND_COUNT' INTEGER ");
			}

			// trade表新增 BANK_CARD_NO 字段
			boolean result3 = false;
			Cursor cursor3 = null;

			try {
				cursor3 = db
						.rawQuery(
								"select * from sqlite_master where name = ? and sql like ?",
								new String[] { "TRADE",
										"%" + "BANK_CARD_NO" + "%" });
				result3 = null != cursor3 && cursor3.moveToFirst();
			} catch (Exception e) {
				Log.e("daoMaster", "checkColumnExists2..." + e.getMessage());
			} finally {
				if (null != cursor3 && !cursor3.isClosed()) {
					cursor3.close();
				}
			}
			if (!result3) {
				db.execSQL("ALTER TABLE 'TRADE' ADD 'BANK_CARD_NO' varchar(32) ");
			}

			// 新增未知条形码表
			db.execSQL("CREATE TABLE IF NOT EXISTS  'UNKNOW_MEDICINE' (" + //
					"'BAR_CODE' TEXT PRIMARY KEY NOT NULL );");

			// 新增文件上传表
			db.execSQL("CREATE TABLE IF NOT EXISTS \"TRADE_FILE\" ("
					+ //
					"\"TRADE_FILE_NAME\" TEXT PRIMARY KEY NOT NULL ,"
					+ "\"TRADE_TYPE\" INTEGER NOT NULL ,"
					+ "\"TRADE_DATE\" INTEGER NOT NULL ,"
					+ "\"UPLOAD_STATUS\" INTEGER NOT NULL );");


			//新增全渠道交易表
			db.execSQL("CREATE TABLE " + "IF NOT EXISTS " + "\"OMNI_TRADE\" (" + //
					"\"OUT_TRADE_NO\" TEXT PRIMARY KEY NOT NULL ," + // 0:
																		// outTradeNo
					"\"BUS_CODE\" TEXT NOT NULL ," + // 1: busCode
					"\"PAY_CHANNEL\" TEXT NOT NULL ," + // 2: payChannel
					"\"MERCHANT_NO\" TEXT NOT NULL ," + // 3: merchantNo
					"\"CANNEL_OUT_TRADE_NO\" TEXT NOT NULL ," + // 4:
																// cannelOutTradeNo
					"\"REFUND_OUT_TRADE_NO\" TEXT NOT NULL ," + // 5:
																// refundOutTradeNo
					"\"TRANSACTION_ID\" TEXT," + // 6: transactionId
					"\"TOTAL_FEE\" TEXT NOT NULL ," + // 7: totalFee
					"\"DATE\" TEXT NOT NULL ," + // 8: date
					"\"PAY_TIME\" TEXT," + // 9: payTime
					"\"FK_TRADE_ID\" TEXT);"); // 10: fkTradeId

		}

	}

	public DaoMaster(SQLiteDatabase db) {
		super(db, SCHEMA_VERSION);
		registerDaoClass(MedicineDao.class);
		registerDaoClass(HealthDao.class);
		registerDaoClass(TradeDao.class);
		registerDaoClass(TradeDetailDao.class);
		registerDaoClass(CardInfoDao.class);
		registerDaoClass(BlackListDao.class);
		registerDaoClass(TempBlackListDao.class);
		registerDaoClass(PolicyDocumentDao.class);
		registerDaoClass(CommandConfirmDao.class);
		registerDaoClass(DocumentDao.class);
		registerDaoClass(UnknowMedicineDao.class);
		registerDaoClass(TradeFileDao.class);
		registerDaoClass(OmniTradeDao.class);
	}

	public DaoSession newSession() {
		return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
	}

	public DaoSession newSession(IdentityScopeType type) {
		return new DaoSession(db, type, daoConfigMap);
	}

}
