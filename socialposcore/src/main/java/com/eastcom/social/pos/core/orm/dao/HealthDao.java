package com.eastcom.social.pos.core.orm.dao;

import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.eastcom.social.pos.core.orm.entity.Health;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class HealthDao extends AbstractDao<Health, String> {

	public static final String TABLENAME = "HEALTH";

	public static class Properties {
		public final static Property Id = new Property(0, String.class, "id",
				true, "_id");
		public final static Property Bar_Code = new Property(1, String.class,
				"bar_code", false, "BAR_CODE");
		public final static Property Name = new Property(2, String.class,
				"name", false, "NAME");
		public final static Property Production_Unit = new Property(3,
				String.class, "production_unit", false, "PRODUCTION_UNIT");
		public final static Property Production_Address = new Property(4,
				String.class, "production_address", false, "PRODUCTION_ADDRESS");
		public final static Property Specification = new Property(5,
				String.class, "specification", false, "SPECIFICATION");
		public final static Property Dosage_Form = new Property(6,
				String.class, "dosage_form", false, "DOSAGE_FORM");
		public final static Property Original_Price = new Property(7,
				Integer.class, "original_price", false, "ORIGINAL_PRICE");
		public final static Property Social_Category = new Property(8,
				Integer.class, "social_category", false, "SOCIAL_CATEGORY");
		public final static Property Health_Function = new Property(9,
				String.class, "health_function", false, "HEALTH_FUNCTION");
		public final static Property Approval_No = new Property(10,
				String.class, "approval_no", false, "APPROVAL_NO");
		public final static Property Approval_Date = new Property(11,
				Date.class, "approval_date", false, "APPROVAL_DATE");
		public final static Property Notes = new Property(12, String.class,
				"notes", false, "NOTES");
	};

	public HealthDao(DaoConfig config) {
		super(config);
	}

	public HealthDao(DaoConfig config, DaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'HEALTH' ("
				+ "'_id' varchar(32) PRIMARY KEY NOT NULL,"
				+ "'BAR_CODE' varchar(32)  NOT NULL,"
				+ "'NAME' varchar(32)  NOT NULL,"
				+ "'PRODUCTION_UNIT' varchar(512)  NOT NULL,"
				+ "'PRODUCTION_ADDRESS' varchar(256)  ,"
				+ "'SPECIFICATION' varchar(256)  NOT NULL,"
				+ "'DOSAGE_FORM' varchar(32)  NOT NULL,"
				+ "'ORIGINAL_PRICE' INTEGER  NOT NULL,"
				+ "'SOCIAL_CATEGORY' INTEGER  NOT NULL,"
				+ "'HEALTH_FUNCTION' varchar(256)  ,"
				+ "'APPROVAL_NO' varchar(32)  ," + "'APPROVAL_DATE' DATE  ,"
				+ "'NOTES' varchar(256));");
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'HEALTH'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, Health entity) {
		stmt.clearBindings();

		stmt.bindString(1, entity.getId());
		stmt.bindString(2, entity.getBar_code());
		stmt.bindString(3, entity.getName());
		stmt.bindString(4, entity.getProduction_unit());
		if (entity.getProduction_address() != null) {
			stmt.bindString(5, entity.getProduction_address());
		}
		stmt.bindString(6, entity.getSpecification());
		stmt.bindString(7, entity.getDosage_form());
		stmt.bindLong(8, entity.getOriginal_price());
		stmt.bindLong(9, entity.getSocial_category());
		if (entity.getHealth_function() != null) {
			stmt.bindString(10, entity.getHealth_function());
		}
		if (entity.getApproval_no() != null) {
			stmt.bindString(11, entity.getApproval_no());
		}
		if (entity.getApproval_date() != null) {
			stmt.bindLong(12, entity.getApproval_date().getTime());
		}
		if (entity.getNotes() != null) {
			stmt.bindString(13, entity.getNotes());
		}

	}

	/** @inheritdoc */
	@Override
	public String readKey(Cursor cursor, int offset) {
		return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
	}

	/** @inheritdoc */
	@Override
	public Health readEntity(Cursor cursor, int offset) {
		Health entity = new Health(
				cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0),
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1),
				cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2),
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3),
				cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4),
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5),
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6),
				cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7),
				cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8),
				cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9),
				cursor.isNull(offset + 10) ? null : cursor
						.getString(offset + 10),
				cursor.isNull(offset + 11) ? null : new java.util.Date(cursor
						.getLong(offset + 11)),
				cursor.isNull(offset + 12) ? null : cursor
						.getString(offset + 12));
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, Health entity, int offset) {
		entity.setId(cursor.isNull(offset + 0) ? null : cursor
				.getString(offset + 0));
		entity.setBar_code(cursor.isNull(offset + 1) ? null : cursor
				.getString(offset + 1));
		entity.setName(cursor.isNull(offset + 2) ? null : cursor
				.getString(offset + 2));
		entity.setProduction_unit(cursor.isNull(offset + 3) ? null : cursor
				.getString(offset + 3));
		entity.setProduction_address(cursor.isNull(offset + 4) ? null : cursor
				.getString(offset + 4));
		entity.setSpecification(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
		entity.setDosage_form(cursor.isNull(offset + 6) ? null : cursor
				.getString(offset + 6));
		entity.setOriginal_price(cursor.isNull(offset + 7) ? null : cursor
				.getInt(offset + 7));
		entity.setSocial_category(cursor.isNull(offset + 8) ? null : cursor
				.getInt(offset + 8));
		entity.setHealth_function(cursor.isNull(offset + 9) ? null : cursor
				.getString(offset + 9));
		entity.setApproval_no(cursor.isNull(offset + 10) ? null : cursor
				.getString(offset + 10));
		entity.setApproval_date(cursor.isNull(offset + 11) ? null
				: new java.util.Date(cursor.getLong(offset + 11)));
		entity.setNotes(cursor.isNull(offset + 12) ? null : cursor
				.getString(offset + 12));
	}

	/** @inheritdoc */
	@Override
	public String getKey(Health entity) {
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
	protected String updateKeyAfterInsert(Health entity, long rowid) {
		// TODO Auto-generated method stub

		return null;
	}

}
