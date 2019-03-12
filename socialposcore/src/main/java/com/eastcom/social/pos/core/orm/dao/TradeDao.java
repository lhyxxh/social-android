package com.eastcom.social.pos.core.orm.dao;

import java.util.Date;

import com.eastcom.social.pos.core.orm.entity.Trade;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class TradeDao extends AbstractDao<Trade, String> {

	public static final String TABLENAME = "TRADE";

	public static class Properties {
		public final static Property Id = new Property(0, String.class, "id",
				true, "_id");
		public final static Property Trade_No = new Property(1, Integer.class,
				"trade_no", false, "TRADE_NO");
		public final static Property Pre_Trade = new Property(2, Integer.class,
				"pre_trade_no", false, "PRE_TRADE_NO");
		public final static Property Sign_Board_No = new Property(3,
				String.class, "sign_board_no", false, "SIGN_BOARD_NO");
		public final static Property Rfsam_No = new Property(4, String.class,
				"rfsam_no", false, "RFSAM_NO");
		public final static Property Psam_No = new Property(5, String.class,
				"psam_no", false, "PSAM_NO");
		public final static Property Terminal_Code = new Property(6,
				String.class, "terminal_code", false, "TERMINAL_CODE");
		public final static Property Trade_Time = new Property(7, Date.class,
				"trade_time", false, "TRADE_TIME");
		public final static Property Ref_No = new Property(8, String.class,
				"ref_no", false, "REF_NO");
		public final static Property Social_Card_No = new Property(9,
				String.class, "social_card_no", false, "SOCIAL_CARD_NO");
		public final static Property Id_Card_No = new Property(10,
				String.class, "id_card_no", false, "ID_CARD_NO");
		public final static Property Trade_Type = new Property(11,
				Integer.class, "trade_type", false, "TRADE_TYPE");
		public final static Property Trade_State = new Property(12,
				Integer.class, "trade_state", false, "TRADE_STATE");
		public final static Property Trade_Money = new Property(13,
				Integer.class, "trade_money", false, "TRADE_MONEY");
		public final static Property Ss_Pay = new Property(14, Integer.class,
				"ss_pay", false, "SS_PAY");
		public final static Property Individual = new Property(15,
				Integer.class, "individual_pay", false, "INDIVIDUAL_PAY");
		public final static Property Is_Upload = new Property(16,
				Integer.class, "is_upload", false, "IS_UPLOAD");
		public final static Property Amount = new Property(17, Integer.class,
				"amount", false, "AMOUNT");
		public final static Property Is_Revoke = new Property(18,
				Integer.class, "is_revoke", false, "IS_REVOKE");
		public final static Property Trace = new Property(19, String.class,
				"trace", false, "TRACE");
		public final static Property Send_Count = new Property(20,
				Integer.class, "send_count", false, "SEND_COUNT");
		public final static Property Bank_Card_No = new Property(21,
				Integer.class, "bank_card_no", false, "BANK_CARD_NO");
		public final static Property Encrypt = new Property(22, String.class,
				"encrypt", false, "ENCRYPT");
	};

	public TradeDao(DaoConfig config) {
		super(config);
	}

	public TradeDao(DaoConfig config, DaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'TRADE' ("
				+ "'_id' varchar(32) NOT NULL PRIMARY KEY ,"
				+ "'TRADE_NO' INTEGER NOT NULL,"
				+ "'PRE_TRADE_NO' INTEGER NOT NULL,"
				+ "'SIGN_BOARD_NO' varchar(32)  NOT NULL,"
				+ "'RFSAM_NO' varchar(32)  NOT NULL,"
				+ "'PSAM_NO' varchar(32)  NOT NULL,"
				+ "'TERMINAL_CODE' varchar(32) NOT NULL ,"
				+ "'TRADE_TIME' DATE  NOT NULL,"
				+ "'REF_NO' varchar(32) NOT NULL ,"
				+ "'SOCIAL_CARD_NO' varchar(32) NOT NULL ,"
				+ "'ID_CARD_NO' varchar(32) NOT NULL ,"
				+ "'TRADE_TYPE' INTEGER  NOT NULL,"
				+ "'TRADE_STATE' INTEGER  NOT NULL,"
				+ "'TRADE_MONEY' INTEGER  NOT NULL,"
				+ "'SS_PAY' INTEGER  NOT NULL,"
				+ "'INDIVIDUAL_PAY' INTEGER  NOT NULL,"
				+ "'IS_UPLOAD' INTEGER  ," + "'AMOUNT' INTEGER NOT NULL  ,"
				+ "'IS_REVOKE' INTEGER  ," + "'TRACE' varchar(32) ,"
				+ "'SEND_COUNT' INTEGER," + " 'BANK_CARD_NO' varchar(32) ,"
				+ "'ENCRYPT' TEXT);");
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TRADE'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, Trade entity) {
		stmt.clearBindings();

		stmt.bindString(1, entity.getId());
		stmt.bindLong(2, entity.getTrade_no());
		stmt.bindLong(3, entity.getPre_trade_no());
		stmt.bindString(4, entity.getSign_board_no());
		stmt.bindString(5, entity.getRfsam_no());
		if (entity.getPsam_no() == null) {
			stmt.bindString(6, "000000");
		} else {
			stmt.bindString(6, entity.getPsam_no());
		}

		stmt.bindString(7, entity.getTerminal_code());
		stmt.bindLong(8, entity.getTrade_time().getTime());
		stmt.bindString(9, entity.getRef_no());
		stmt.bindString(10, entity.getSocial_card_no());
		stmt.bindString(11, entity.getId_card_no());
		stmt.bindLong(12, entity.getTrade_type());
		stmt.bindLong(13, entity.getTrade_state());
		stmt.bindLong(14, entity.getTrade_money());
		stmt.bindLong(15, entity.getSs_pay());
		stmt.bindLong(16, entity.getIndividual_pay());
		stmt.bindLong(17, entity.getIs_upload());
		stmt.bindLong(18, entity.getAmount());
		stmt.bindLong(19, entity.getIs_revoke());
		stmt.bindString(20, entity.getTrace());
		stmt.bindLong(21, entity.getSend_count());
		if (entity.getBank_card_no() == null) {
			stmt.bindString(22, "00000000000000000000");
		} else {
			stmt.bindString(22, entity.getBank_card_no());
		}
		String encrypt = entity.getEncrypt();
		if (encrypt != null) {
			stmt.bindString(23, encrypt);
		}

	}

	/** @inheritdoc */
	@Override
	public String readKey(Cursor cursor, int offset) {
		return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
	}

	/** @inheritdoc */
	@Override
	public Trade readEntity(Cursor cursor, int offset) {
		Trade entity = new Trade(
				cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0),
				cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1),
				cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2),
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3),
				cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4),
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5),
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6),
				cursor.isNull(offset + 7) ? null : new java.util.Date(cursor
						.getLong(offset + 7)), cursor.isNull(offset + 8) ? null
						: cursor.getString(offset + 8), cursor
						.isNull(offset + 9) ? null : cursor
						.getString(offset + 9),
				cursor.isNull(offset + 10) ? null : cursor
						.getString(offset + 10),
				cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11),
				cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12),
				cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13),
				cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14),
				cursor.isNull(offset + 15) ? null : cursor.getInt(offset + 15),
				cursor.isNull(offset + 16) ? null : cursor.getInt(offset + 16),
				cursor.isNull(offset + 17) ? null : cursor.getInt(offset + 17),
				cursor.isNull(offset + 18) ? 0 : cursor.getInt(offset + 18),
				cursor.isNull(offset + 19) ? null : cursor
						.getString(offset + 19), cursor.isNull(offset + 20) ? 0
						: cursor.getInt(offset + 20), cursor
						.isNull(offset + 21) ? null : cursor
						.getString(offset + 21),
				cursor.isNull(offset + 22) ? null : cursor
						.getString(offset + 22));
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, Trade entity, int offset) {
		entity.setId(cursor.isNull(offset + 0) ? null : cursor
				.getString(offset + 0));
		entity.setTrade_no(cursor.isNull(offset + 1) ? null : cursor
				.getInt(offset + 1));
		entity.setPre_trade_no(cursor.isNull(offset + 2) ? null : cursor
				.getInt(offset + 2));
		entity.setSign_board_no(cursor.isNull(offset + 3) ? null : cursor
				.getString(offset + 3));
		entity.setRfsam_no(cursor.isNull(offset + 4) ? null : cursor
				.getString(offset + 4));
		entity.setPsam_no(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
		entity.setTerminal_code(cursor.isNull(offset + 6) ? null : cursor
				.getString(offset + 6));
		entity.setTrade_time(cursor.isNull(offset + 7) ? null
				: new java.util.Date(cursor.getLong(offset + 7)));
		entity.setRef_no(cursor.isNull(offset + 8) ? null : cursor
				.getString(offset + 8));
		entity.setSocial_card_no(cursor.isNull(offset + 9) ? null : cursor
				.getString(offset + 9));
		entity.setId_card_no(cursor.isNull(offset + 10) ? null : cursor
				.getString(offset + 10));
		entity.setTrade_type(cursor.isNull(offset + 11) ? null : cursor
				.getInt(offset + 11));
		entity.setTrade_state(cursor.isNull(offset + 12) ? null : cursor
				.getInt(offset + 12));
		entity.setTrade_money(cursor.isNull(offset + 13) ? null : cursor
				.getInt(offset + 13));
		entity.setSs_pay(cursor.isNull(offset + 14) ? null : cursor
				.getInt(offset + 14));
		entity.setIndividual_pay(cursor.isNull(offset + 15) ? null : cursor
				.getInt(offset + 15));
		entity.setIs_upload(cursor.isNull(offset + 16) ? null : cursor
				.getInt(offset + 16));
		entity.setAmount(cursor.isNull(offset + 17) ? null : cursor
				.getInt(offset + 17));
		entity.setIs_revoke(cursor.isNull(offset + 18) ? 0 : cursor
				.getInt(offset + 18));
		entity.setTrace(cursor.isNull(offset + 19) ? null : cursor
				.getString(offset + 19));
		entity.setSend_count(cursor.isNull(offset + 20) ? 0 : cursor
				.getInt(offset + 20));
		entity.setBank_card_no(cursor.isNull(offset + 21) ? null : cursor
				.getString(offset + 21));
		entity.setEncrypt(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
	}

	/** @inheritdoc */
	@Override
	protected String updateKeyAfterInsert(Trade entity, long rowId) {
		String id = entity.getId();
		return id;
	}

	/** @inheritdoc */
	@Override
	public String getKey(Trade entity) {
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
