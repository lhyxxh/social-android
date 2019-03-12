package com.eastcom.social.pos.core.orm.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.eastcom.social.pos.core.orm.entity.TradeFile;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TRADE_FILE".
*/
public class TradeFileDao extends AbstractDao<TradeFile, String> {

    public static final String TABLENAME = "TRADE_FILE";

    /**
     * Properties of entity TradeFile.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property TradeFileName = new Property(0, String.class, "tradeFileName", true, "TRADE_FILE_NAME");
        public final static Property TradeType = new Property(1, int.class, "tradeType", false, "TRADE_TYPE");
        public final static Property TradeDate = new Property(2, java.util.Date.class, "tradeDate", false, "TRADE_DATE");
        public final static Property UploadStatus = new Property(3, int.class, "uploadStatus", false, "UPLOAD_STATUS");
    }


    public TradeFileDao(DaoConfig config) {
        super(config);
    }
    
    public TradeFileDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TRADE_FILE\" (" + //
                "\"TRADE_FILE_NAME\" TEXT PRIMARY KEY NOT NULL ," + // 0: tradeFileName
                "\"TRADE_TYPE\" INTEGER NOT NULL ," + // 1: tradeType
                "\"TRADE_DATE\" INTEGER NOT NULL ," + // 2: tradeDate
                "\"UPLOAD_STATUS\" INTEGER NOT NULL );"); // 3: uploadStatus
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TRADE_FILE\"";
        db.execSQL(sql);
    }


    @Override
    protected final void bindValues(SQLiteStatement stmt, TradeFile entity) {
        stmt.clearBindings();
        stmt.bindString(1, entity.getTradeFileName());
        stmt.bindLong(2, entity.getTradeType());
        stmt.bindLong(3, entity.getTradeDate().getTime());
        stmt.bindLong(4, entity.getUploadStatus());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.getString(offset + 0);
    }    

    @Override
    public TradeFile readEntity(Cursor cursor, int offset) {
        TradeFile entity = new TradeFile( //
            cursor.getString(offset + 0), // tradeFileName
            cursor.getInt(offset + 1), // tradeType
            new java.util.Date(cursor.getLong(offset + 2)), // tradeDate
            cursor.getInt(offset + 3) // uploadStatus
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TradeFile entity, int offset) {
        entity.setTradeFileName(cursor.getString(offset + 0));
        entity.setTradeType(cursor.getInt(offset + 1));
        entity.setTradeDate(new java.util.Date(cursor.getLong(offset + 2)));
        entity.setUploadStatus(cursor.getInt(offset + 3));
     }
    
    @Override
    protected final String updateKeyAfterInsert(TradeFile entity, long rowId) {
        return entity.getTradeFileName();
    }
    
    @Override
    public String getKey(TradeFile entity) {
        if(entity != null) {
            return entity.getTradeFileName();
        } else {
            return null;
        }
    }


    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
