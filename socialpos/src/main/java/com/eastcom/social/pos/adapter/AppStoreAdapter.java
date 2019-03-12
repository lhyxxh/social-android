package com.eastcom.social.pos.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.database.DatabaseService;
import com.eastcom.social.pos.entity.AppListEntity;
import com.eastcom.social.pos.entity.AppNewListEntity;
import com.eastcom.social.pos.entity.DeviceAppInfoEntity;
import com.eastcom.social.pos.minterface.DownloadCallback;
import com.eastcom.social.pos.service.UpdateManager;

public class AppStoreAdapter extends BaseAdapter implements OnClickListener{

	private static final String TAG = "AppStoreAdapter";
	private int APP_PAGE_SIZE ;  //每页显示item的个数
	private int page = 0;
	private ArrayList<AppNewListEntity> list;
	private LayoutInflater inflater;
	private Context context;
	// 查看设备各APP信息版本号
	private ArrayList<DeviceAppInfoEntity> mlistAppInfo = null;// 本地APP信息
	private Callback mCallback;
	private UpdateManager updateManager;
	private Map<String, String> uploadLogoMap = new HashMap<String, String>();

	private boolean isUploadLogo = false;//记录是否已经下载了logo
	private static String dir = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/SocialStore/logo/";
	
	
	public interface Callback {
		void itemInnerClick(View v);
	}
	 
	public AppStoreAdapter(Context context, ArrayList<AppListEntity> list,
			ArrayList<DeviceAppInfoEntity> mlistAppInfo,Callback callback,
			int page,int size) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.page = page;
		this.mlistAppInfo = mlistAppInfo;
		this.APP_PAGE_SIZE = size;
		
		int i = page * APP_PAGE_SIZE;  
        int iEnd = i+APP_PAGE_SIZE;  
        while ((i<list.size()) && (i<iEnd)) {  
        	list.add(list.get(i));  
            i++;  
        }  
        this.list = getNewInfoList(list);
	
		mCallback = callback;
		updateManager = new UpdateManager(context);
		
	}

	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_app_store, null);
			holder = new ViewHolder();
			holder.appstore_lay1 = (LinearLayout) convertView.findViewById(R.id.appstore_lay1);
			holder.appstore_lay2 = (LinearLayout) convertView.findViewById(R.id.appstore_lay2);
			holder.appstore_lay3 = (LinearLayout) convertView.findViewById(R.id.appstore_lay3);
			holder.appstore_name = (TextView) convertView.findViewById(R.id.appstore_name);
			holder.appstore_detail = (TextView) convertView.findViewById(R.id.appstore_detail);
			holder.appstore_logo = (ImageView) convertView.findViewById(R.id.appstore_logo);
			holder.icon_installed = (ImageView) convertView.findViewById(R.id.icon_installed);
			holder.appstore_install1 = (TextView) convertView.findViewById(R.id.appstore_install1);
			holder.appstore_install1.setOnClickListener(this);
			holder.appstore_open2 = (TextView) convertView.findViewById(R.id.appstore_open2);
			holder.appstore_open2.setOnClickListener(this);
			holder.appstore_uninstall2 = (TextView) convertView.findViewById(R.id.appstore_uninstall2);
			holder.appstore_uninstall2.setOnClickListener(this);
			holder.appstore_update3 = (TextView) convertView.findViewById(R.id.appstore_update3);
			holder.appstore_update3.setOnClickListener(this);
			holder.appstore_uninstall3= (TextView) convertView.findViewById(R.id.appstore_uninstall3);
			holder.appstore_uninstall3.setOnClickListener(this);
			holder.appstore_open3 = (TextView) convertView.findViewById(R.id.appstore_open3);
			holder.appstore_open3.setOnClickListener(this);
			
//			for (int k=page*APP_PAGE_SIZE; k<(page+1)*APP_PAGE_SIZE; k++)
			int temp_position = position + APP_PAGE_SIZE*page;
			holder.appstore_install1.setTag(temp_position);
			holder.appstore_open2.setTag(temp_position);
			holder.appstore_uninstall2.setTag(temp_position);
			holder.appstore_update3.setTag(temp_position);
			holder.appstore_uninstall3.setTag(temp_position);
			holder.appstore_open3.setTag(temp_position);
			
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		try {
				if (list.get(position).getIsInstalled()==0) {
					holder.icon_installed.setVisibility(View.GONE);
					showLayout(holder,1);
				} else {// 已安装--再判断是否有更新选择
					if(APP_PAGE_SIZE==9){
						holder.icon_installed.setVisibility(View.VISIBLE);
					}
					if (list.get(position).getIsNewVersion()==0) {// 无更新
						showLayout(holder,2);
					} else {// 有更新
						showLayout(holder,3);
					}
				}
				if(list.get(position).getIsUploadLogo()){
//					try {
						holder.appstore_logo.setImageURI(Uri.fromFile(new File(dir
								+ list.get(position).getAppName()+".jpg")));
						Log.d(TAG, "取出的值为更新控件-getAppName-》》"+list.get(position).getAppName()
									+"此时的position   "+position);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
				}
			holder.appstore_name.setText(list.get(position).getAppName());
			holder.appstore_detail.setText(list.get(position).getAppRemark());
		} catch (Exception e) {
			e.printStackTrace(); 
			Log.e(TAG, "getView 抛异常---"+e.toString());
		}
		try {
			if(!isUploadLogo){
				for(Object obj : uploadLogoMap.keySet()){
					Object value = uploadLogoMap.get(obj );
					String id = value.toString().split(",")[0];
					String name = value.toString().split(",")[1];
					String flag = "logo";
					Log.e(TAG, "请求下载Logo--id--"+id+"--name--"+name);
					updateManager.checkUpdateInfo(id, flag, name);
					Thread.sleep(100);
				}
				isUploadLogo = true;
			}
			updateManager.setDownloadCallback(new DownloadCallback(){
				@Override
				public void isDownloadPhotoSucess(boolean isSuccess) {
				}
				@Override
				public void isDownloadLogoSucess(boolean isSuccess) {
					if(isSuccess){
						notifyDataSetChanged();
					}
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return convertView;
	}

	private void showLayout(ViewHolder holder, int i) {
		switch(i){
		case 1:
			holder.appstore_lay1.setVisibility(View.VISIBLE);
			holder.appstore_lay2.setVisibility(View.GONE);
			holder.appstore_lay3.setVisibility(View.GONE);
			break;
		case 2:
			holder.appstore_lay1.setVisibility(View.GONE);
			holder.appstore_lay2.setVisibility(View.VISIBLE);
			holder.appstore_lay3.setVisibility(View.GONE);
			break;
		case 3:
			holder.appstore_lay1.setVisibility(View.GONE);
			holder.appstore_lay2.setVisibility(View.GONE);
			holder.appstore_lay3.setVisibility(View.VISIBLE);
			break;
		}
	}

	public final class ViewHolder {
		public TextView appstore_name1, appstore_detail1,appstore_name2, appstore_detail2,
						appstore_name3, appstore_detail3,
						appstore_name, appstore_detail;
		public TextView appstore_update3, appstore_open3, appstore_uninstall3,
				appstore_open2, appstore_uninstall2, appstore_install1;
		private ImageView appstore_logo, icon_installed;
		private LinearLayout appstore_lay1, appstore_lay2, appstore_lay3;
	}

	/**
	 * 根据获取应用市场的APP list刷新应用市场列表
	 * 
	 * @param appList
	 */
	public void changeValue(ArrayList<AppListEntity> appList) {
		if (list != null && !list.isEmpty()) {
			Log.e(TAG, "changeValue--list.size()--"+list.size());
			list.clear();
		}
		this.list.addAll(getNewInfoList(appList));
		notifyDataSetChanged();
	}

	//将  是否安装  及  是否有更新  两个字段一起加入，构成新的entity给adapter更新数据
	private ArrayList<AppNewListEntity> getNewInfoList(ArrayList<AppListEntity> appList2) {
		ArrayList<AppNewListEntity> newList = null;
		if (appList2 != null) {
			newList = new ArrayList<AppNewListEntity>();
			
			for (int k=page*APP_PAGE_SIZE; k<(page+1)*APP_PAGE_SIZE; k++) {
				//避免最后一页载入多余的数据
				if(k<(appList2.size()-APP_PAGE_SIZE*(page+1))){
					AppNewListEntity newAppList = new AppNewListEntity();
					String appId = appList2.get(k).getId();
					String packageName = appList2.get(k).getPackageName();
					String appName = appList2.get(k).getAppName();
					String appRemark = appList2.get(k).getAppRemark();
					String versionName = appList2.get(k).getVersionName();
					int versionCode = appList2.get(k).getVersionCode();
					int uploadLogo = appList2.get(k).getUploadLogo();
					newAppList.setId(appId);
					newAppList.setPackageName(packageName);
					newAppList.setAppName(appName);
					newAppList.setAppRemark(appRemark);
					newAppList.setVersionName(versionName);
					newAppList.setVersionCode(versionCode);
					newAppList.setUploadLogo(uploadLogo);
					if(appList2.get(k).getUploadLogo()==1){
						uploadLogoMap.put(appList2.get(k).getId(), appList2.get(k).getId()
									+","+appList2.get(k).getAppName());
						newAppList.setIsUploadLogo(true);
					}
					for (int i = 0; i < mlistAppInfo.size(); i++) {
						if (!appList2.get(k).getPackageName().
								equals(mlistAppInfo.get(i).getPkgName())) {//没有安装
							newAppList.setIsInstalled(0);
						} else {// 已安装--再判断是否有更新选择
							newAppList.setIsInstalled(1);
							String tableName = "social_myApp";
							DatabaseService.insertAppStore(context, tableName, appId, packageName,
									appName, appRemark, versionName, versionCode, uploadLogo);
//							DatabaseService.cleanAppStore(context, tableName);
							if (appList2.get(k).getVersionName().
									equals(mlistAppInfo.get(i).getVersionName())) {// 无更新
								newAppList.setIsNewVersion(0);
								break;
							} else {// 有更新
								newAppList.setIsNewVersion(1);
								break;
							}
						}
					}
					
					String tableName = "social_appStore";
					DatabaseService.insertAppStore(context, tableName, appId, packageName,
							appName, appRemark, versionName, versionCode, uploadLogo);
					newList.add(newAppList);
//					DatabaseService.cleanAppStore(context, tableName);
				}
			}
			
		}
		return newList;
	}
	
	@Override
	public void onClick(View v) {
		mCallback.itemInnerClick(v);
	}
	
}
