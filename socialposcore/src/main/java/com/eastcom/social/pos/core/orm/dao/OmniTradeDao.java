package com.eastcom.social.pos.core.orm.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.eastcom.social.pos.core.orm.entity.OmniTrade;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table "OMNI_TRADE".
 */
public class OmniTradeDao extends AbstractDao<OmniTrade, String> {

	public static final String TABLENAME = "OMNI_TRADE";

	/**
	 * Properties of entity OmniTrade.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property OutTradeNo = new Property(0, String.class,
				"outTradeNo", true, "OUT_TRADE_NO");
		public final static Property BusCode = new Property(1, String.class,
				"busCode", false, "BUS_CODE");
		public final static Property PayChannel = new Property(2, String.class,
				"payChannel", false, "PAY_CHANNEL");
		public final static Property MerchantNo = new Property(3, String.class,
				"merchantNo", false, "MERCHANT_NO");
		public final static Property CannelOutTradeNo = new Property(4,
				String.class, "cannelOutTradeNo", false, "CANNEL_OUT_TRADE_NO");
		public final static Property RefundOutTradeNo = new Property(5,
				String.class, "refundOutTradeNo", false, "REFUND_OUT_TRADE_NO");
		public final static Property TransactionId = new Property(6,
				String.class, "transactionId", false, "TRANSACTION_ID");
		public final static Property TotalFee = new Property(7, String.class,
				"totalFee", false, "TOTAL_FEE");
		public final static Property Date = new Property(8, String.class,
				"date", false, "DATE");
		public final static Property PayTime = new Property(9, String.class,
				"payTime", false, "PAY_TIME");
	}

	public OmniTradeDao(DaoConfig config) {
		super(config);
	}

	public OmniTradeDao(DaoConfig config, DaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "\"OMNI_TRADE\" (" + //
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
				"\"PAY_TIME\" TEXT);"); // 9: payTime
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "\"OMNI_TRADE\"";
		db.execSQL(sql);
	}

	@Override
	protected final void bindValues(SQLiteStatement stmt, OmniTrade entity) {
		stmt.clearBindings();

		String outTradeNo = entity.getOutTradeNo();
		if (outTradeNo != null) {
			stmt.bindString(1, outTradeNo);
		}
		stmt.bindString(2, entity.getBusCode());
		stmt.bindString(3, entity.getPayChannel());
		stmt.bindString(4, entity.getMerchantNo());
		stmt.bindString(5, entity.getCannelOutTradeNo());
		stmt.bindString(6, entity.getRefundOutTradeNo());

		String transactionId = entity.getTransactionId();
		if (transactionId != null) {
			stmt.bindString(7, transactionId);
		}
		stmt.bindString(8, entity.getTotalFee());
		stmt.bindString(9, entity.getDate());

		String payTime = entity.getPayTime();
		if (payTime != null) {
			stmt.bindString(10, payTime);
		}
	}

	@Override
	public String readKey(Cursor cursor, int offset) {
		return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
	}

	@Override
	public OmniTrade readEntity(Cursor cursor, int offset) {
		OmniTrade entity = new OmniTrade(
				//
				cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // outTradeNo
				cursor.getString(offset + 1), // busCode
				cursor.getString(offset + 2), // payChannel
				cursor.getString(offset + 3), // merchantNo
				cursor.getString(offset + 4), // cannelOutTradeNo
				cursor.getString(offset + 5), // refundOutTradeNo
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // transactionId
				cursor.getString(offset + 7), // totalFee
				cursor.getString(offset + 8), // date
				cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // payTime
		);
		return entity;
	}

	@Override
	public void readEntity(Cursor cursor, OmniTrade entity, int offset) {
		entity.setOutTradeNo(cursor.isNull(offset + 0) ? null : cursor
				.getString(offset + 0));
		entity.setBusCode(cursor.getString(offset + 1));
		entity.setPayChannel(cursor.getString(offset + 2));
		entity.setMerchantNo(cursor.getString(offset + 3));
		entity.setCannelOutTradeNo(cursor.getString(offset + 4));
		entity.setRefundOutTradeNo(cursor.getString(offset + 5));
		entity.setTransactionId(cursor.isNull(offset + 6) ? null : cursor
				.getString(offset + 6));
		entity.setTotalFee(cursor.getString(offset + 7));
		entity.setDate(cursor.getString(offset + 8));
		entity.setPayTime(cursor.isNull(offset + 9) ? null : cursor
				.getString(offset + 9));
	}

	@Override
	protected final String updateKeyAfterInsert(OmniTrade entity, long rowId) {
		return entity.getOutTradeNo();
	}

	@Override
	public String getKey(OmniTrade entity) {
		if (entity != null) {
			return entity.getOutTradeNo();
		} else {
			return null;
		}
	}

	@Override
	protected final boolean isEntityUpdateable() {
		return true;
	}

}