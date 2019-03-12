package com.eastcom.social.pos.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.eastcom.social.pos.entity.AppListEntity;

public class DatabaseService {

	private static final String TAG = "DatabaseService";
	private static DatabaseHelper dbHelper=null;
	private static SharedPreferences share;
	private static SharedPreferences.Editor editor;
	private static String myAppTableName="social_myApp";
	private static String appStoreTableName = "social_appStore";

	public static void cleanAppStore(Context context, String tableName) {
		dbHelper = new DatabaseHelper(context,tableName);
//		Log.i(TAG, "删除数据库缓存---》》》");
		dbHelper.del();
		saveSharePreference(tableName,false);
		dbHelper.close();
	}
	
	//判断表格是否存在
	public static boolean isTableExist(Context context, String tableName) {
		dbHelper = new DatabaseHelper(context,tableName);
		dbHelper.createTable();
		Cursor cursor = dbHelper.queryTable(tableName);
		while(cursor.moveToNext()){
			//遍历出表名
			String name = cursor.getString(0);
			if(name.equals(tableName)){
				return true;
			}
		}
		return false;
	}
	
	//判断表格是否存在
	public static void deleteData(Context context, String tableName, String[] appId) {
			try {
				dbHelper = new DatabaseHelper(context,tableName);
				dbHelper.createTable();
				dbHelper.delData("appId=? ",  appId );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	//往数据库中插入数据
	public static void insertAppStore(Context context, String tableName,String appId,
	String packageName, String appName,	String appRemark,String versionName,
	int versionCode,int uploadLogo) {
		try {
			share = context.getSharedPreferences("social_app_info", 0);
			editor = share.edit();
			dbHelper = new DatabaseHelper(context,tableName);
			dbHelper.createTable();
			Cursor cursor = dbHelper.queryAppId("appId=? ", new String[] { appId });
//			Log.i(TAG, "准备存数据到数据库---11  》》》"+appId);
			if(cursor.moveToNext()){
				while(cursor.moveToNext()){
//					Log.i(TAG, "准备存数据到数据库-22--》》》");
					//遍历出表中的appId值，比较是否已经存在：若存在，则不再存储
					String appId1 = cursor.getString(cursor.getColumnIndex("appId"));
//					Log.i(TAG, "准备存数据到数据库---appId1  》》》"+appId1);
					if(!appId1.equals(appId)){
//						Log.i(TAG, "准备存数据到数据库-33--》》》");
						dbHelper.insert(getContentValues(appId,packageName,appName,
								appRemark,versionName,versionCode,uploadLogo));
						saveSharePreference(tableName,true);
					}
				}
			}else{
//				Log.i(TAG, "准备存数据到数据库-44--》》》");
				dbHelper.insert(getContentValues(appId,packageName,appName,
						appRemark,versionName,versionCode,uploadLogo));
				saveSharePreference(tableName,true);
			}
			dbHelper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//appId,packageName,appName,appRemark,versionName,versionCode,uploadLogo
	public static ContentValues getContentValues(String appId,String packageName,
		String appName,String appRemark,String versionName,int versionCode,int uploadLogo) {
			ContentValues cv = new ContentValues();
			cv.put("appId", appId);
			cv.put("packageName", packageName);
			cv.put("appName", appName);
			cv.put("appRemark", appRemark);
			cv.put("versionName", versionName);
			cv.put("versionCode", versionCode);
			cv.put("uploadLogo", uploadLogo);
//			Log.e(TAG, "准备存储  getContentValues appId>>"+appId);
		return cv;
	}

	//从数据库中提取数据，整个表格的数据
	public static ArrayList<AppListEntity> getMyAppFromDatabase(Context context,
		String tableName) {
			ArrayList<AppListEntity> list = new ArrayList<AppListEntity>();
			if (dbHelper == null) {
				dbHelper = new DatabaseHelper(context,tableName);
				dbHelper.createTable();
			}
//			Log.i(TAG, "getMyAppFromDatabase 准备取出数据库数据---《《《");
			 
			Cursor cursor = dbHelper.query(tableName);
			
			while(cursor.moveToNext()) {
				AppListEntity entity = new AppListEntity();
				int appId = cursor.getColumnIndex("appId");
				int packageName = cursor.getColumnIndex("packageName");
				int appName = cursor.getColumnIndex("appName");
				int appRemark = cursor.getColumnIndex("appRemark");
				int versionName = cursor.getColumnIndex("versionName");
				int versionCode = cursor.getColumnIndex("versionCode");
				int uploadLogo = cursor.getColumnIndex("uploadLogo");
//				Log.e(TAG, "getMyAppFromDatabase   appId====" +cursor.getString(appId));
				entity.setId(cursor.getString(appId));
				entity.setPackageName(cursor.getString(packageName));
				entity.setAppName(cursor.getString(appName));
				entity.setAppRemark(cursor.getString(appRemark));
				entity.setVersionName(cursor.getString(versionName));
				entity.setVersionCode(cursor.getInt(versionCode));
				entity.setUploadLogo(cursor.getInt(uploadLogo));
				list.add(entity);
			}
			dbHelper.close();
		return list;
	}
	
	//从数据库中提取整个表格的数据，并判断appName是否包含搜索的字段，若包含则加入到列表
		public static ArrayList<AppListEntity> getSearchAppFromDatabase(Context context,
			String tableName,String search) {
				ArrayList<AppListEntity> list = new ArrayList<AppListEntity>();
				if (dbHelper == null) {
					dbHelper = new DatabaseHelper(context,tableName);
					dbHelper.createTable();
				}
//				Log.i(TAG, "getSearchAppFromDatabase 准备取出数据库数据---《《《");
				 
				Cursor cursor = dbHelper.query(tableName);
				
				while(cursor.moveToNext()) {
					AppListEntity entity = new AppListEntity();
					int appId = cursor.getColumnIndex("appId");
					int packageName = cursor.getColumnIndex("packageName");
					int appName = cursor.getColumnIndex("appName");
					int appRemark = cursor.getColumnIndex("appRemark");
					int versionName = cursor.getColumnIndex("versionName");
					int versionCode = cursor.getColumnIndex("versionCode");
					int uploadLogo = cursor.getColumnIndex("uploadLogo");
					if(cursor.getString(appName).contains(search)){
//						Log.e(TAG, "getMyAppFromDatabase   appId====" +cursor.getString(appId));
						entity.setId(cursor.getString(appId));
						entity.setPackageName(cursor.getString(packageName));
						entity.setAppName(cursor.getString(appName));
						entity.setAppRemark(cursor.getString(appRemark));
						entity.setVersionName(cursor.getString(versionName));
						entity.setVersionCode(cursor.getInt(versionCode));
						entity.setUploadLogo(cursor.getInt(uploadLogo));
						list.add(entity);
					}
				}
				dbHelper.close();
			return list;
		}
	private static void saveSharePreference(String tableName,boolean is){
		if(tableName.equals(appStoreTableName)){
			if(is){
				editor.putBoolean("isSaveAppStoreTable", true);
			}else{
				editor.putBoolean("isSaveAppStoreTable", false);
			}
			editor.commit();
		}else if(tableName.equals(myAppTableName)){
			if(is){
				editor.putBoolean("isSaveMyAppTable", true);
			}else{
				editor.putBoolean("isSaveMyAppTable", false);
			}
			editor.commit();
		}
	}
}
