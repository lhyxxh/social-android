package com.eastcom.social.pos.core.orm.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.eastcom.social.pos.core.orm.entity.PolicyDocument;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table POLICY_DOCUMENT.
*/
public class PolicyDocumentDao extends AbstractDao<PolicyDocument, String> {

    public static final String TABLENAME = "POLICY_DOCUMENT";

    /**
     * Properties of entity PolicyDocument.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property CreateDate = new Property(1, String.class, "createDate", false, "CREATE_DATE");
        public final static Property UpdateDate = new Property(2, String.class, "updateDate", false, "UPDATE_DATE");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property VersionCode = new Property(4, String.class, "versionCode", false, "VERSION_CODE");
        public final static Property FileNo = new Property(5, String.class, "fileNo", false, "FILE_NO");
        public final static Property PublishDate = new Property(6, String.class, "publishDate", false, "PUBLISH_DATE");
        public final static Property IsPublishing = new Property(7, String.class, "isPublishing", false, "IS_PUBLISHING");
        public final static Property PublishStatusLbl = new Property(8, String.class, "publishStatusLbl", false, "PUBLISH_STATUS_LBL");
    };


    public PolicyDocumentDao(DaoConfig config) {
        super(config);
    }
    
    public PolicyDocumentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'POLICY_DOCUMENT' (" + //
                "'ID' TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "'CREATE_DATE' TEXT," + // 1: createDate
                "'UPDATE_DATE' TEXT," + // 2: updateDate
                "'NAME' TEXT," + // 3: name
                "'VERSION_CODE' TEXT," + // 4: versionCode
                "'FILE_NO' TEXT," + // 5: fileNo
                "'PUBLISH_DATE' TEXT," + // 6: publishDate
                "'IS_PUBLISHING' TEXT," + // 7: isPublishing
                "'PUBLISH_STATUS_LBL' TEXT);"); // 8: publishStatusLbl
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'POLICY_DOCUMENT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PolicyDocument entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String createDate = entity.getCreateDate();
        if (createDate != null) {
            stmt.bindString(2, createDate);
        }
 
        String updateDate = entity.getUpdateDate();
        if (updateDate != null) {
            stmt.bindString(3, updateDate);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        String versionCode = entity.getVersionCode();
        if (versionCode != null) {
            stmt.bindString(5, versionCode);
        }
 
        String fileNo = entity.getFileNo();
        if (fileNo != null) {
            stmt.bindString(6, fileNo);
        }
 
        String publishDate = entity.getPublishDate();
        if (publishDate != null) {
            stmt.bindString(7, publishDate);
        }
 
        String isPublishing = entity.getIsPublishing();
        if (isPublishing != null) {
            stmt.bindString(8, isPublishing);
        }
 
        String publishStatusLbl = entity.getPublishStatusLbl();
        if (publishStatusLbl != null) {
            stmt.bindString(9, publishStatusLbl);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PolicyDocument readEntity(Cursor cursor, int offset) {
        PolicyDocument entity = new PolicyDocument( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // createDate
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // updateDate
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // versionCode
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // fileNo
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // publishDate
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // isPublishing
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // publishStatusLbl
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PolicyDocument entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCreateDate(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUpdateDate(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setVersionCode(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFileNo(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPublishDate(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIsPublishing(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPublishStatusLbl(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(PolicyDocument entity, long rowId) {
        return entity.getId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(PolicyDocument entity) {
        if(entity != null) {
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
