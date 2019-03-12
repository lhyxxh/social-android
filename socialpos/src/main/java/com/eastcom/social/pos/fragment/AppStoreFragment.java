package com.eastcom.social.pos.fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.eastcom.social.pos.R;
import com.eastcom.social.pos.activity.AppDetailActivity;
import com.eastcom.social.pos.activity.AppStoreActivity;
import com.eastcom.social.pos.adapter.AppStoreAdapter;
import com.eastcom.social.pos.adapter.AppStoreAdapter.Callback;
import com.eastcom.social.pos.component.PageControlView;
import com.eastcom.social.pos.component.ScrollLayout;
import com.eastcom.social.pos.component.ScrollLayout.OnScreenChangeListenerDataLoad;
import com.eastcom.social.pos.database.DatabaseService;
import com.eastcom.social.pos.entity.AppListEntity;
import com.eastcom.social.pos.entity.DeviceAppInfoEntity;
import com.eastcom.social.pos.service.DataFactory;
import com.eastcom.social.pos.service.UpdateManager;
import com.eastcom.social.pos.util.QueryDeviceAppInfo;
import com.eastcom.social.pos.util.ToastUtil;


 public class AppStoreFragment extends Fragment implements Callback {

	private static final String TAG = "AppStoreFragment";
	public AppStoreActivity mActivity;
	private Context context;
	private EditText et_search_appstore;
	private RelativeLayout search_appstore;
	
	private ScrollLayout scroll_layout;  
    private static final int APP_PAGE_SIZE = 9;  
    private PageControlView page_control; 
    private DataLoading dataLoad;  

	private SharedPreferences share;
	private ArrayList<AppListEntity> appList;
	private AppStoreAdapter adapter;
	
	private unInstallReceiver unInstallReceiver;
    private installReceiver installReceiver;
	private String pkgName="", appId = "", appName="", appRemark="", versionName="";
    private int versionCode, uploadLogo;
	
	
	// 查看设备各APP信息版本号
	private ArrayList<DeviceAppInfoEntity> mlistAppInfo = null;// 本地APP信息

	private GetDataTask getDataTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
     
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_app_store, container,false);
		mlistAppInfo = new ArrayList<DeviceAppInfoEntity>();
		intiView(view);
		initData();
		registerunInstallReceiver();
		registerInstallReceiver();

		return view;
	}
	
	private void intiView(View view) {
		et_search_appstore = (EditText) view.findViewById(R.id.et_search_appstore);
		search_appstore = (RelativeLayout) view.findViewById(R.id.search_appstore);
		search_appstore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String tableName = "social_appStore";
				String search = et_search_appstore.getText().toString().replaceAll(" ", "");
				Log.e(TAG, "搜索  search-->>"+search);
				if(!"".equals(search)&&search!=null){
					Log.e(TAG, "搜索  search-->>准备查找");
					appList = DatabaseService.getSearchAppFromDatabase(context, 
							tableName, search);
					Log.e(TAG, "搜索  search-appList.size()->>"+appList.size());
					if(appList.size()!=0){
						pageReflesh();
					}else{
						ToastUtil.show(context, getResources()
								.getString(R.string.search_error), 5000);
					}
				}else{//若搜索时，搜索框为空，则获取表中所有数据
					appList = DatabaseService.getMyAppFromDatabase(context, tableName);
					if(appList.size()!=0){
						pageReflesh();
					}
				}
			}
		});
		
		scroll_layout = (ScrollLayout)view.findViewById(R.id.scroll_layout); 
		page_control = (PageControlView) view.findViewById(R.id.page_control);  
		dataLoad = new DataLoading();  
	}
	
	private void initData() {
		context = this.getActivity().getApplicationContext();
		share = context.getSharedPreferences("social_app_info", 0);
		appList = new ArrayList<AppListEntity>();
		boolean isSaveAppStoreTable = share.getBoolean("isSaveAppStoreTable", false);
		if(isSaveAppStoreTable){//若已经存储了  应用市场 到数据库，则先获取本地数据库展示界面
			String tableName = "social_appStore";
			if(DatabaseService.isTableExist(context,tableName)){
				appList = DatabaseService.getMyAppFromDatabase(context, tableName);
				if(appList.size()!=0){
					Log.e(TAG, "initData  先 本地数据库");
					pageReflesh();
				}
			}
		}
		Log.e(TAG, "initData  再 请求网络数据");
		startGetDataTask();
		
	}

	private void registerunInstallReceiver() {
		unInstallReceiver = new unInstallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        getActivity().registerReceiver(unInstallReceiver, filter);
	}

	private void registerInstallReceiver() {
		installReceiver = new installReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");
        getActivity().registerReceiver(installReceiver, filter);
	}
	
	private void pageReflesh() {
        try {
			int pageNo = (int)Math.ceil( appList.size()/APP_PAGE_SIZE)+1; 
            //由于每次 安装 或者 卸载 之后，本地的mlistAppInfo 发生了改变 ，更新该列表以刷新界面
			mlistAppInfo = QueryDeviceAppInfo.queryDeviceAppInfo(getActivity(),"1");
			scroll_layout.removeAllViewsInLayout();//先置空，以免再次加入
			page_control.removeAllViews();
			for (int i = 0; i < pageNo; i++) {  
			    GridView appPage = new GridView(context);  
				adapter = new AppStoreAdapter(context,appList, mlistAppInfo, this,i,APP_PAGE_SIZE);
				appPage.setAdapter(adapter);
			    appPage.setNumColumns(3);  //列数
			    appPage.setVerticalSpacing(20);
			    appPage.setHorizontalSpacing(10);
			    appPage.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View v,
							int position, long id) {
						 Intent detailIntent = new Intent(getActivity(),AppDetailActivity.class);
						 detailIntent.putExtra("appId", appList.get(position).getId());
						 detailIntent.putExtra("appName", appList.get(position).getAppName());
						 detailIntent.putExtra("appRemark", appList.get(position).getAppRemark());
						 startActivity(detailIntent);
					}
				});  
			    scroll_layout.addView(appPage);  
			}  
			//加载分页  
			page_control.bindScrollViewGroup(scroll_layout);
			//加载分页数据  
            dataLoad.bindScrollViewGroup(scroll_layout);	
			adapter.changeValue(appList);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG,"pageReflesh 错误--"+e.toString());
		}
		
	}
	@Override
	public void itemInnerClick(View v) {
		//appId,pkgName,appName, appRemark, versionName, versionCode, uploadLogo
		UpdateManager updateManager;
		updateManager = new UpdateManager(getActivity());
		appId = appList.get((Integer)v.getTag()).getId();
		pkgName = appList.get((Integer)v.getTag()).getPackageName();
		appName = appList.get((Integer)v.getTag()).getAppName();
		appRemark = appList.get((Integer)v.getTag()).getAppRemark();
		versionName = appList.get((Integer)v.getTag()).getVersionName();
		versionCode = appList.get((Integer)v.getTag()).getVersionCode();
		uploadLogo = appList.get((Integer)v.getTag()).getUploadLogo();
		
		
		switch (v.getId()) {
		case R.id.appstore_update3:
			String flag = "apk";
			updateManager.checkUpdateInfo(appId,flag,appName);
			break;
		case R.id.appstore_open3:
			updateManager.openApp(pkgName);
			break;
		case R.id.appstore_uninstall3:
			updateManager.unInstallApk(pkgName);
			break;
		case R.id.appstore_open2:
			updateManager.openApp(pkgName);
			break;
		case R.id.appstore_uninstall2:
			updateManager.unInstallApk(pkgName);
			break;
		case R.id.appstore_install1:
			String flag1 = "apk";
			updateManager.checkUpdateInfo(appId,flag1,appName);
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
		private ArrayList<AppListEntity> tempAppList = new ArrayList<AppListEntity>();
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
					DataFactory dataFactory = new DataFactory();
					JSONObject json = new JSONObject(dataFactory.getAppList());
					JSONArray jsonArray = json.getJSONArray("list");
					Log.e(TAG, "取得的数据为----"+json.toString());
					AppListEntity entity = new AppListEntity();
					entity.setAppListEntity(jsonArray, tempAppList);
			} catch (Exception e) {
				getDataTask = null;
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			getDataTask = null;
			try {
				if(result){
					appList.clear();
					for (int i = 0; i < tempAppList.size(); i++) {
						appList.add(tempAppList.get(i));
					}
					pageReflesh();
					
				}else{
					Log.e(TAG,"onPostExecute result---"+result);
				}
			} catch (Exception e) {
				Log.e(TAG, "onPostExecute  异常抛出--"+e.toString());
			}
		}

	}

	//分页数据  
    class DataLoading {  
        private int count;  
        public void bindScrollViewGroup(ScrollLayout scrollViewGroup) {  
            this.count=scrollViewGroup.getChildCount();  
            scrollViewGroup.setOnScreenChangeListenerDataLoad(new OnScreenChangeListenerDataLoad() {  
                public void onScreenChange(int currentIndex) {  
                    //generatePageControl(currentIndex);  
                }  
            });  
        }  
        private void generatePageControl(int currentIndex){  
            if(count==currentIndex+1){  
            }  
        }  
    }  
    
    
    private class unInstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        if (pkgName != null && ("package:" + pkgName).equals(intent.getDataString())) {
    		String tableName = "social_appStore";
    		String myAppTableName = "social_myApp";
    		
            //收到卸载完成后的广播才删除该条记录，并刷新列表
			appList = DatabaseService.getMyAppFromDatabase(context, tableName);
			if(appList.size()!=0){
				pageReflesh();
			}
			//在应用市场卸载程序，应该将我的应用中对应程序在数据库中的位置 也删除掉
			DatabaseService.deleteData(context, myAppTableName, new String[] { appId });
        }
      }
    }
    
    private class installReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        if (pkgName != null && ("package:" + pkgName).equals(intent.getDataString())) {
        	String tableName = "social_appStore";
    		String myAppTableName = "social_myApp";
    		
			appList = DatabaseService.getMyAppFromDatabase(context, tableName);
			if(appList.size()!=0){
				pageReflesh();
			}
			//在应用市场安装程序，应该将该应用列表存储到 我的应用的 数据库中
			DatabaseService.insertAppStore(context, myAppTableName, appId, pkgName,
					appName, appRemark, versionName, versionCode, uploadLogo);
        }
      }
    }
    
    @Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(unInstallReceiver);
		getActivity().unregisterReceiver(installReceiver);
	}
}
