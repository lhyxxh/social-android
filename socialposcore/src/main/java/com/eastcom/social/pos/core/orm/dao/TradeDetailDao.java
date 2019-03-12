package com.eastcom.social.pos.core.orm.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.eastcom.social.pos.core.orm.entity.TradeDetail;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class TradeDetailDao extends AbstractDao<TradeDetail, String> {

	public static final String TABLENAME = "TRADE_DETAIL";

	public static class Properties {
		public final static Property Id = new Property(0, String.class, "id",
				true, "_id");
		public final static Property Fk_Trade_Id = new Property(1,
				String.class, "fk_trade_id", false, "FK_TRADE_ID");
		public final static Property Detail_Trade_No = new Property(2,
				Integer.class, "detail_trade", false, "DETAIL_TRADE_NO");
		public final static Property Bar_Code = new Property(3, String.class,
				"bar_code", false, "BAR_CODE");
		public final static Property Super_Vision_Code = new Property(4,
				String.class, "super_vision_code", false, "SUPER_VISION_CODE");
		public final static Property Actual_Price = new Property(5,
				Integer.class, "actual_price", false, "ACTUAL_PRICE");
		public final static Property Amount = new Property(6, Integer.class,
				"amount", false, "AMOUNT");
		public final static Property Social_Category = new Property(7,
				Integer.class, "social_category", false, "SOCIAL_CATEGORY");
		public final static Property Is_Upload = new Property(8, Integer.class,
				"is_upload", false, "IS_UPLOAD");
		public final static Property Product_Name = new Property(9,
				Integer.class, "product_name", false, "PRODUCT_NAME");
		public final static Property Trade_Type = new Property(10,
				Integer.class, "trade_type", false, "TRADE_TYPE");
	};

	public TradeDetailDao(DaoConfig config) {
		super(config);
	}

	public TradeDetailDao(DaoConfig config, DaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'TRADE_DETAIL' ("
				+ "'_id' varchar(32) NOT NULL PRIMARY KEY ,"
				+ "'FK_TRADE_ID' varchar(32) NOT NULL,"
				+ "'DETAIL_TRADE_NO' INTEGER NOT NULL,"
				+ "'BAR_CODE' varchar(32)  NOT NULL,"
				+ "'SUPER_VISION_CODE' varchar(32)  ,"
				+ "'ACTUAL_PRICE' INTEGER  NOT NULL,"
				+ "'AMOUNT' INTEGER NOT NULL ,"
				+ "'SOCIAL_CATEGORY' INTEGER  NOT NULL,"
				+ "'IS_UPLOAD' INTEGER," + "'PRODUCT_NAME' varchar(512),"
				+ "'TRADE_TYPE' INTEGER );");
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'TRADE_DETAIL'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, TradeDetail entity) {
		stmt.clearBindings();

		stmt.bindString(1, entity.getId());
		stmt.bindString(2, entity.getFk_trade_id());
		stmt.bindLong(3, entity.getDetail_trade());
		stmt.bindString(4, entity.getBar_code());
		if (entity.getSuper_vision_code() != null) {
			stmt.bindString(5, entity.getSuper_vision_code());
		}
		stmt.bindLong(6, entity.getActual_price());
		stmt.bindLong(7, entity.getAmount());
		stmt.bindLong(8, entity.getSocial_category());
		stmt.bindLong(9, entity.getIs_upload());
		stmt.bindString(10, entity.getProduct_name());
		stmt.bindLong(11, entity.getTrade_type());
	}

	/** @inheritdoc */
	@Override
	public String readKey(Cursor cursor, int offset) {
		return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
	}

	/** @inheritdoc */
	@Override
	public TradeDetail readEntity(Cursor cursor, int offset) {
		TradeDetail entity = new TradeDetail(
				cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0),
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1),
				cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2),
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3),
				cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4),
				cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5),
				cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6),
				cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7),
				cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8),
				cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9),
				cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, TradeDetail entity, int offset) {
		entity.setId(cursor.isNull(offset + 0) ? null : cursor
				.getString(offset + 0));
		entity.setFk_trade_id(cursor.isNull(offset + 1) ? null : cursor
				.getString(offset + 1));
		entity.setDetail_trade(cursor.isNull(offset + 2) ? null : cursor
				.getInt(offset + 2));
		entity.setBar_code(cursor.isNull(offset + 3) ? null : cursor
				.getString(offset + 3));
		entity.setSuper_vision_code(cursor.isNull(offset + 4) ? null : cursor
				.getString(offset + 4));
		entity.setActual_price(cursor.isNull(offset + 5) ? null : cursor
				.getInt(offset + 5));
		entity.setAmount(cursor.isNull(offset + 6) ? null : cursor
				.getInt(offset + 6));
		entity.setSocial_category(cursor.isNull(offset + 7) ? null : cursor
				.getInt(offset + 7));
		entity.setIs_upload(cursor.isNull(offset + 8) ? null : cursor
				.getInt(offset + 8));
		entity.setProduct_name(cursor.isNull(offset + 9) ? null : cursor
				.getString(offset + 9));
		entity.setTrade_type(cursor.isNull(offset + 10) ? null : cursor
				.getInt(offset + 10));
	}

	/** @inheritdoc */
	@Override
	protected String updateKeyAfterInsert(TradeDetail entity, long rowId) {
		return null;
	}

	/** @inheritdoc */
	@Override
	public String getKey(TradeDetail entity) {
		if (entity != null) {
			return entity.getId();
		} else {
			return null;
		}
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

}
