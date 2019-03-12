package com.eastcom.social.pos.core.orm.dao;

import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.eastcom.social.pos.core.orm.entity.CardInfo;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class CardInfoDao extends AbstractDao<CardInfo, String> {

	public static final String TABLENAME = "CARDINFO";

	public static class Properties {
		public final static Property Id = new Property(0, String.class, "id",
				true, "_id");
		public final static Property Bank_no = new Property(1, String.class,
				"bank_no", false, "BANK_NO");
		public final static Property Social_card_no = new Property(2,
				String.class, "social_card_no", false, "SOCIAL_CARD_NO");
		public final static Property Id_card_no = new Property(3, String.class,
				"id_card_no", false, "ID_CARD_NO");
		public final static Property Name = new Property(4, String.class,
				"name", false, "NAME");
		public final static Property Id_code = new Property(5, String.class,
				"id_code", false, "ID_CODE");
		public final static Property Sex = new Property(6, String.class, "sex",
				false, "SEX");
		public final static Property StartDate = new Property(7, Date.class,
				"startDate", false, "STARTDATE");
		public final static Property EndDtae = new Property(8, Date.class,
				"endDate", false, "ENDDTAE");
	};

	public CardInfoDao(DaoConfig config) {
		super(config);
	}

	public CardInfoDao(DaoConfig config, DaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'CARDINFO' ("
				+ "'_id' varchar(256) PRIMARY KEY NOT NULL,"
				+ "'BANK_NO' varchar(256)  NOT NULL,"
				+ "'SOCIAL_CARD_NO' varchar(256)  NOT NULL,"
				+ "'ID_CARD_NO' varchar(256)  NOT NULL,"
				+ "'NAME' varchar(256)  NOT NULL,"
				+ "'ID_CODE' varchar(256)  NOT NULL,"
				+ "'SEX' varchar(256)  NOT NULL,"
				+ "'STARTDATE' DATE  NOT NULL," + "'ENDDTAE' DATE  NOT NULL);");
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'CARDINFO'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, CardInfo entity) {
		stmt.clearBindings();

		stmt.bindString(1, entity.getId());
		stmt.bindString(2, entity.getBank_no());
		stmt.bindString(3, entity.getSocial_card_no());
		stmt.bindString(4, entity.getId_card_no());
		stmt.bindString(5, entity.getName());
		stmt.bindString(6, entity.getId_code());
		stmt.bindString(7, entity.getSex());
		stmt.bindLong(8, entity.getStartDate().getTime());
		stmt.bindLong(9, entity.getEndDtae().getTime());

	}

	/** @inheritdoc */
	@Override
	public String readKey(Cursor cursor, int offset) {
		return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
	}

	/** @inheritdoc */
	@Override
	public CardInfo readEntity(Cursor cursor, int offset) {
		CardInfo entity = new CardInfo(
				cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0),
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1),
				cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2),
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3),
				cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4),
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5),
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6),
				cursor.isNull(offset + 7) ? null : new java.util.Date(cursor
						.getLong(offset + 7)), cursor.isNull(offset + 8) ? null
						: new java.util.Date(cursor.getLong(offset + 8)));
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, CardInfo entity, int offset) {
		entity.setId(cursor.isNull(offset + 0) ? null : cursor
				.getString(offset + 0));
		entity.setBank_no(cursor.isNull(offset + 1) ? null : cursor
				.getString(offset + 1));
		entity.setSocial_card_no(cursor.isNull(offset + 2) ? null : cursor
				.getString(offset + 2));
		entity.setId_card_no(cursor.isNull(offset + 3) ? null : cursor
				.getString(offset + 3));
		entity.setName(cursor.isNull(offset + 4) ? null : cursor
				.getString(offset + 4));
		entity.setId_code(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
		entity.setSex(cursor.isNull(offset + 6) ? null : cursor
				.getString(offset + 6));
		entity.setStartDate(cursor.isNull(offset + 7) ? null
				: new java.util.Date(cursor.getLong(offset + 7)));
		entity.setEndDtae(cursor.isNull(offset + 8) ? null
				: new java.util.Date(cursor.getLong(offset + 8)));
	}

	/** @inheritdoc */
	@Override
	public String getKey(CardInfo entity) {
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

	@Override
	protected String updateKeyAfterInsert(CardInfo entity, long rowid) {
		// TODO Auto-generated method stub

		return null;
	}

}
