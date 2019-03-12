package com.eastcom.social.pos.core.orm.dao;

import com.eastcom.social.pos.core.orm.entity.TempBlackList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class TempBlackListDao extends AbstractDao<TempBlackList, String> {

    public static final String TABLENAME = "TEMP_BLACK_LIST";

    /**
     * Properties of entity TempBlackList.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property SocialCardNo = new Property(0, String.class, "SocialCardNo", true, "SOCIAL_CARD_NO");
        public final static Property Time = new Property(1, java.util.Date.class, "Time", false, "TIME");
    };


    public TempBlackListDao(DaoConfig config) {
        super(config);
    }
    
    public TempBlackListDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TEMP_BLACK_LIST' (" + //
                "'SOCIAL_CARD_NO' TEXT PRIMARY KEY NOT NULL ," + // 0: SocialCardNo
                "'TIME' INTEGER);"); // 1: Time
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TEMP_BLACK_LIST'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TempBlackList entity) {
        stmt.clearBindings();
 
        String SocialCardNo = entity.getSocialCardNo();
        if (SocialCardNo != null) {
            stmt.bindString(1, SocialCardNo);
        }
 
        java.util.Date Time = entity.getTime();
        if (Time != null) {
            stmt.bindLong(2, Time.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public TempBlackList readEntity(Cursor cursor, int offset) {
        TempBlackList entity = new TempBlackList( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // SocialCardNo
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)) // Time
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TempBlackList entity, int offset) {
        entity.setSocialCardNo(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setTime(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(TempBlackList entity, long rowId) {
        return entity.getSocialCardNo();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(TempBlackList entity) {
        if(entity != null) {
            return entity.getSocialCardNo();
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
