package com.eastcom.social.pos.activity;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.entity.AppDetailEntity;
import com.eastcom.social.pos.minterface.DownloadCallback;
import com.eastcom.social.pos.service.DataFactory;
import com.eastcom.social.pos.service.UpdateManager;

public class AppDetailActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = "AppDetailActivity";
	private TextView app_name, app_description, app_version, update_time,
			app_size, update_detail, update_now;
	private ImageView back, photo_right, photo_center, photo_left;

	private String appId, appName,appRemark;
	private GetDataTask getDataTask;

	private UpdateManager updateManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		initView();
		initData();

	}

	private void initView() {
		app_name = (TextView) this.findViewById(R.id.app_name);
		app_description = (TextView) this.findViewById(R.id.app_description);
		app_version = (TextView) this.findViewById(R.id.app_version);
		update_time = (TextView) this.findViewById(R.id.update_time);
		app_size = (TextView) this.findViewById(R.id.app_size);
		update_detail = (TextView) this.findViewById(R.id.update_detail);
		update_now = (TextView) this.findViewById(R.id.update_now);
		back = (ImageView) this.findViewById(R.id.back);
		photo_right = (ImageView) this.findViewById(R.id.photo_right);
		photo_center = (ImageView) this.findViewById(R.id.photo_center);
		photo_left = (ImageView) this.findViewById(R.id.photo_left);

		back.setOnClickListener(this);
		update_now.setOnClickListener(this);
	}

	private void initData() {

		Intent detailIntent = getIntent();
		appId = detailIntent.getStringExtra("appId");
		appName = detailIntent.getStringExtra("appName");
		appRemark = detailIntent.getStringExtra("appRemark");
		startGetDataTask();

		updateManager = new UpdateManager(AppDetailActivity.this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.update_now:
			String flag = "apk";
			updateManager.checkUpdateInfo(appId, flag, appName);
			break;
		}
	}

	public void startGetDataTask() {

		if (null == getDataTask) {
			getDataTask = new GetDataTask();
			getDataTask.execute();
		}
	}

	public class GetDataTask extends AsyncTask<Void, Void, Boolean> {
		JSONObject jsonObject;
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				ArrayList<AppDetailEntity> appDetailList = new ArrayList<AppDetailEntity>();
				
				DataFactory dataFactory = new DataFactory();
				JSONObject json = new JSONObject( dataFactory.getAppDetail(appId));// 返回json对象

				if ("true".equals(json.getString("success"))) {
					jsonObject = json.getJSONObject("item");
					Log.d(TAG, "jsonObject   "+jsonObject);
//					AppDetailEntity entity = new AppDetailEntity();
//					entity.setAppDetailEntity(jsonObject, appDetailList);
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				getDataTask = null;

				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			getDataTask = null;

			try {
				if (result) {

					updateUI(jsonObject);
				}
				Log.e(TAG, "onPostExecute result---" + result);
			} catch (Exception e) {
				Log.e(TAG, "onPostExecute  异常抛出--" + e.toString());
			}
		}

		private void updateUI(JSONObject jsonObject) {
			try {
				float size = (jsonObject.optLong("fileSize"))/1024/1024;
				size  =  (float)(Math.round(size*10))/10;
				app_name.setText(appName);
				app_description.setText(appRemark);
				app_version.setText(getResources().getString(R.string.app_version)
						+jsonObject.optString("versionName"));
				update_time.setText(getResources().getString(R.string.update_time)
						+jsonObject.optString("updateDate").substring(0, 10));
				app_size.setText(getResources().getString(R.string.app_size)
						+String.valueOf(size)+getResources().getString(R.string.level_M));
				update_detail.setText(getResources().getString(R.string.update_detail)
						+jsonObject.optString("versionRemark"));
				int uploadPhoto = jsonObject.optInt("uploadPhoto");
				
				if(uploadPhoto==1){
					//有照片可更新，准备下载照片
					String flag = "photo";
					updateManager.checkUpdateInfo(appId, flag, appName);
					updateManager.setDownloadCallback(new DownloadCallback() {

						@Override
						public void isDownloadPhotoSucess(boolean isSuccess) {
							Log.e(TAG, "photoMap.size()--"+updateManager.photoMap.size());
							if(updateManager.downloadPhotoSucces){
								Log.d(TAG, "下载并解压完成后photo成功返回");
								int photoMapSize = updateManager.photoMap.size();
								if(photoMapSize!=0){
									photo_right.setBackgroundDrawable(null);
									photo_center.setBackgroundDrawable(null);
									photo_left.setBackgroundDrawable(null);
									photo_right.setImageURI(null);
									photo_center.setImageURI(null);
									photo_left.setImageURI(null);
								}
								switch(photoMapSize){
								case 1:
									photo_center.setImageURI(Uri.fromFile(new 
											File(updateManager.photoMap.get(0))));
									break;
								case 2:
									photo_right.setImageURI(Uri.fromFile(new 
											File(updateManager.photoMap.get(0))));
									photo_center.setImageURI(Uri.fromFile(new 
											File(updateManager.photoMap.get(1))));
									break;
								default:
									photo_right.setImageURI(Uri.fromFile(new 
											File(updateManager.photoMap.get(0))));
									photo_center.setImageURI(Uri.fromFile(new 
											File(updateManager.photoMap.get(1))));
									photo_left.setImageURI(Uri.fromFile(new 
											File(updateManager.photoMap.get(2))));
									break;
								}
							}
						}

						@Override
						public void isDownloadLogoSucess(boolean isSuccess) {
						}
					}
					);
				}
			} catch (NotFoundException e) {
				e.printStackTrace();
				Log.e(TAG, "updateUI NotFoundException  "+e.toString());
			}
		}

	}

	@Override
	public void Nodata(boolean nodata) {
		
	}

	@Override
	public void setTime(String time) {
		// TODO Auto-generated method stub
		
	}

	



}
