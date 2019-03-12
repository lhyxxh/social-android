package com.eastcom.social.pos.core.orm.dao;

import java.util.Date;

import com.eastcom.social.pos.core.orm.entity.Medicine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * DAO for table MEDICINE.
 */
public class MedicineDao extends AbstractDao<Medicine, String> {

	public static final String TABLENAME = "MEDICINE";

	/**
	 * Properties of entity Note.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property Id = new Property(0, String.class, "id",
				true, "_id");
		public final static Property Bar_Code = new Property(1, String.class,
				"bar_code", false, "BAR_CODE");
		public final static Property Name = new Property(2, String.class,
				"name", false, "NAME");
		public final static Property Eng_Name = new Property(3, String.class,
				"eng_name", false, "ENG_NAME");
		public final static Property Trade_Name = new Property(4, String.class,
				"trade_name", false, "TRADE_NAME");
		public final static Property Production_Unit = new Property(5,
				String.class, "production_unit", false, "PRODUCTION_UNIT");
		public final static Property Production_Address = new Property(6,
				String.class, "production_address", false, "PRODUCTION_ADDRESS");
		public final static Property Specification = new Property(7,
				String.class, "specification", false, "SPECIFICATION");
		public final static Property Dosage_Form = new Property(8,
				String.class, "dosage_form", false, "DOSAGE_FORM");
		public final static Property Original_Price = new Property(9,
				Integer.class, "original_price", false, "ORIGINAL_PRICE");
		public final static Property Recipe_Category = new Property(10,
				Integer.class, "recipe_category", false, "RECIPE_CATEGORY");
		public final static Property Social_Category = new Property(11,
				Integer.class, "social_category", false, "SOCIAL_CATEGORY");
		public final static Property Approval_No = new Property(12,
				String.class, "approval_no", false, "APPROVAL_NO");
		public final static Property Approval_Date = new Property(13,
				Date.class, "approval_date", false, "APPROVAL_DATE");
		public final static Property Notes = new Property(14, String.class,
				"notes", false, "NOTES");
	};

	public MedicineDao(DaoConfig config) {
		super(config);
	}

	public MedicineDao(DaoConfig config, DaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'MEDICINE' ("
				+ "'_id' varchar(32) PRIMARY KEY NOT NULL,"
				+ "'BAR_CODE' varchar(32)  NOT NULL,"
				+ "'NAME' varchar(512)  NOT NULL,"
				+ "'ENG_NAME' varchar(512)  ," + "'TRADE_NAME' varchar(256)  ,"
				+ "'PRODUCTION_UNIT' varchar(256)  NOT NULL,"
				+ "'PRODUCTION_ADDRESS' varchar(256)  ,"
				+ "'SPECIFICATION' varchar(32)  NOT NULL,"
				+ "'DOSAGE_FORM' varchar(32) NOT NULL ,"
				+ "'ORIGINAL_PRICE' INTEGER  NOT NULL,"
				+ "'RECIPE_CATEGORY' INTEGER  NOT NULL,"
				+ "'SOCIAL_CATEGORY' INTEGER  NOT NULL,"
				+ "'APPROVAL_NO' varchar(32)  ," + "'APPROVAL_DATE' DATE  ,"
				+ "'NOTES' varchar(256));");
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'MEDICINE'";
		db.execSQL(sql);
	}
	
	public static void updateTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "UPDATE TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'MEDICINE'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, Medicine entity) {
		stmt.clearBindings();
		stmt.bindString(1, entity.getId());
		stmt.bindString(2, entity.getBar_code());
		stmt.bindString(3, entity.getName());
		if (entity.getEng_name() != null) {
			stmt.bindString(4, entity.getEng_name());
		}
		if (entity.getTrade_name() != null) {
			stmt.bindString(5, entity.getTrade_name());
		}
		stmt.bindString(6, entity.getProduction_unit());
		if (entity.getProduction_address() != null) {
			stmt.bindString(7, entity.getProduction_address());
		}
		if (entity.getSpecification() != null) {
			stmt.bindString(8, entity.getSpecification());
		}
		if (entity.getDosage_form() != null) {
			stmt.bindString(9, entity.getDosage_form());
		}
		stmt.bindLong(10, entity.getOriginal_price());
		stmt.bindLong(11, entity.getRecipe_category());
		stmt.bindLong(12, entity.getSocial_category());
		if (entity.getApproval_no() != null) {
			stmt.bindString(13, entity.getApproval_no());
		}
		if (entity.getApproval_date() != null) {
			stmt.bindLong(14, entity.getApproval_date().getTime());
		}
		if (entity.getNotes() != null) {
			stmt.bindString(15, entity.getNotes());
		}

	}

	/** @inheritdoc */
	@Override
	public String readKey(Cursor cursor, int offset) {
		return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
	}

	/** @inheritdoc */
	@Override
	public Medicine readEntity(Cursor cursor, int offset) {
		Medicine entity = new Medicine(
				cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0),
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1),
				cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2),
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3),
				cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4),
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5),
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6),
				cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7),
				cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8),
				cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9),
				cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10),
				cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11),
				cursor.isNull(offset + 12) ? null : cursor
						.getString(offset + 12),
				cursor.isNull(offset + 13) ? null : new java.util.Date(cursor
						.getLong(offset + 13)),
				cursor.isNull(offset + 14) ? null : cursor
						.getString(offset + 14));
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, Medicine entity, int offset) {
		entity.setId(cursor.isNull(offset + 0) ? null : cursor
				.getString(offset + 0));
		entity.setBar_code(cursor.isNull(offset + 1) ? null : cursor
				.getString(offset + 1));
		entity.setName(cursor.isNull(offset + 2) ? null : cursor
				.getString(offset + 2));
		entity.setEng_name(cursor.isNull(offset + 3) ? null : cursor
				.getString(offset + 3));
		entity.setTrade_name(cursor.isNull(offset + 4) ? null : cursor
				.getString(offset + 4));
		entity.setProduction_unit(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
		entity.setProduction_address(cursor.isNull(offset + 6) ? null : cursor
				.getString(offset + 6));
		entity.setSpecification(cursor.isNull(offset + 7) ? null : cursor
				.getString(offset + 7));
		entity.setDosage_form(cursor.isNull(offset + 8) ? null : cursor
				.getString(offset + 8));
		entity.setOriginal_price(cursor.isNull(offset + 9) ? null : cursor
				.getInt(offset + 9));
		entity.setRecipe_category(cursor.isNull(offset + 10) ? null : cursor
				.getInt(offset + 10));
		entity.setSocial_category(cursor.isNull(offset + 11) ? null : cursor
				.getInt(offset + 11));
		entity.setApproval_no(cursor.isNull(offset + 12) ? null : cursor
				.getString(offset + 12));
		entity.setApproval_date(cursor.isNull(offset + 13) ? null
				: new java.util.Date(cursor.getLong(offset + 13)));
		entity.setNotes(cursor.isNull(offset + 14) ? null : cursor
				.getString(offset + 14));
	}

	/** @inheritdoc */
	@Override
	protected String updateKeyAfterInsert(Medicine entity, long rowId) {
		return null;
	}

	/** @inheritdoc */
	@Override
	public String getKey(Medicine entity) {
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
