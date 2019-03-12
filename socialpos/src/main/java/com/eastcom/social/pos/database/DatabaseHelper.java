package com.eastcom.social.pos.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 两张表：social_myApp  , social_appStore；
 *	 social_myApp中的条目来自于social_appStore，表格中的参数名称和类型都是一样的
 * 
 * 从应用市场AppList直接获取的，直接存在social_appStore中
 * 
 * 存在social_myApp：
 * 			1、获取AppList列表时，发现设备中已经安装了该应用程序；
 * 			2、用户成功安装该程序；
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	private final static String TAG="DatabaseHelper";
	private static final String DB_NAME = "socialApp.db";

	private String tName="";//两张表：social_myApp  , social_appStore

	public DatabaseHelper(Context context, String tName) {
		super(context, DB_NAME, null, 1);
		this.tName = tName;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			String CREATE_TBL = " create table if not exists "
				+ tName
				+ "(id integer primary key autoincrement, appId varchar(100) NOT NULL unique," 
				+ " packageName varchar(100), appName varchar(100), appRemark varchar(200)," 
				+ " versionName varchar(20), versionCode integer(20), uploadLogo integer(10)) ";
			db.execSQL(CREATE_TBL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增
	 * @param values
	 */
	public void insert(ContentValues values) {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.insert(tName, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除整张表格
	 * 
	 */
	public void del() {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL("DROP TABLE "+tName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除某条记录
	 * @param whereClause
	 * @param whereArgs
	 */
	public void delData(String whereClause, String[] whereArgs) {
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(tName, whereClause, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 改
	 * @param cv
	 * @param whereClause
	 * @param whereArgs
	 */
	public void update(ContentValues cv, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		db.update(tName, cv, whereClause, whereArgs);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	/**
	 * 查寻该表格的所有数据
	 * @param tableName
	 */
	public Cursor query(String tableName) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c= db.rawQuery("select * from " + tableName , null);
		return c;
	}

	/**
	 * 查寻该表格的appId字段
	 * @param whereClause
	 * @param whereArgs
	 */
	public Cursor queryAppId(String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c= db.rawQuery(
					"select * from " + tName + " where " + whereClause, whereArgs);
		return c;
	}
		
	/**
	 * 查寻表格是否存在---查询表格的名称
	 * @return
	 */
	public Cursor queryTable(String tableName) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select name from sqlite_master where type='table';", null);
		return cursor;
	}
	
	/**
	 * 如果表没有被创建，则重新建表
	 */
	public void createTable() {
		SQLiteDatabase db=getWritableDatabase();
		try {
			db.execSQL("select count(*) from " + tName);
		} catch (Exception e) {
			String CREATE_TBL = " create table if not exists "
				+ tName
				+ "(id integer primary key autoincrement, appId varchar(100) NOT NULL unique," 
				+ " packageName varchar(100), appName varchar(100), appRemark varchar(200)," 
				+ " versionName varchar(20), versionCode integer(20), uploadLogo integer(10)) ";
			db.execSQL(CREATE_TBL);
		}
	}
	
	
}