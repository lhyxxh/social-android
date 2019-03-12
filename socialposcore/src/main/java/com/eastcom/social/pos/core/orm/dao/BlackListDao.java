package com.eastcom.social.pos.core.orm.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.eastcom.social.pos.core.orm.entity.BlackList;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class BlackListDao extends AbstractDao<BlackList, String> {

    public static final String TABLENAME = "BLACK_LIST";

    /**
     * Properties of entity BlackList.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property SocialCardNo = new Property(0, String.class, "SocialCardNo", true, "SOCIAL_CARD_NO");
        public final static Property Status = new Property(1, Integer.class, "Status", false, "STATUS");
    };


    public BlackListDao(DaoConfig config) {
        super(config);
    }
    
    public BlackListDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'BLACK_LIST' (" + //
                "'SOCIAL_CARD_NO' TEXT PRIMARY KEY NOT NULL ," + // 0: SocialCardNo
                "'STATUS' INTEGER);"); // 1: Status
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BLACK_LIST'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, BlackList entity) {
        stmt.clearBindings();
 
        String SocialCardNo = entity.getSocialCardNo();
        if (SocialCardNo != null) {
            stmt.bindString(1, SocialCardNo);
        }
 
        Integer Status = entity.getStatus();
        if (Status != null) {
            stmt.bindLong(2, Status);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public BlackList readEntity(Cursor cursor, int offset) {
        BlackList entity = new BlackList( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // SocialCardNo
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1) // Status
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, BlackList entity, int offset) {
        entity.setSocialCardNo(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setStatus(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(BlackList entity, long rowId) {
        return entity.getSocialCardNo();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(BlackList entity) {
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
